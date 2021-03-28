package io.github.awesomestcode.futurehacks.userinterface;

import io.github.awesomestcode.futurehacks.QueryHandler;
import io.github.awesomestcode.futurehacks.factualquery.JokeResolver;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class WebInterfaceServer implements Runnable {

    static final File WEB_ROOT = new File(".");
    static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "not_supported.html";
    // port to listen connection
    static final int PORT = 8080;

    // verbose mode
    static final byte verbosity = 5;

    // Client Connection via Socket Class
    private final Socket connect;

    private static String sendQueryPage;
    private static String resultPage;


    static {
        URL url = WebInterfaceServer.class.getClassLoader().getResource("sendquery.html");
        URL url2 = WebInterfaceServer.class.getClassLoader().getResource("listen.html");
        Path path = null;

        try {
            final Map<String, String> env = new HashMap<>();
            final String[] array = url.toString().split("!");
            final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);
            path = fs.getPath(array[1]);
            sendQueryPage = Files.readString(path, StandardCharsets.UTF_8);
            fs.close();

            final Map<String, String> env2 = new HashMap<>();
            final String[] array2 = url2.toString().split("!");
            final FileSystem fs2 = FileSystems.newFileSystem(URI.create(array[0]), env2);
            path = fs2.getPath(array2[1]);
            resultPage = Files.readString(path, StandardCharsets.UTF_8);
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something seriously went wrong when loading the UI. As this is a critical part of the application, it is now self-destructing your machine.");
            System.exit(1);
        }
    }

    public WebInterfaceServer(Socket c) {
        connect = c;
    }

    public static void main() {
        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

            Desktop desktop = java.awt.Desktop.getDesktop();
            try {
                //specify the protocol along with the URL
                URI oURL = new URI(
                        "http://localhost:8080");
                desktop.browse(oURL);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            // listen until user halts server execution
            while (true) {
                WebInterfaceServer myServer = new WebInterfaceServer(serverConnect.accept());

                if (verbosity > 0) {
                    System.out.println("Connection opened. (" + new Date() + ")");
                }

                // create dedicated thread to manage the client connection
                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        // manage our particular client connection
        BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {
            // read characters from the client via input stream on the socket
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            // get character output stream to client (for headers)
            out = new PrintWriter(connect.getOutputStream());
            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // get first line of the request from the client
            String input = in.readLine();
            // parse the request with a string tokenizer
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // get the HTTP method of the client
            // get file requested
            fileRequested = parse.nextToken().toLowerCase();

            // support only GET and HEAD methods, check
            if (!method.equals("GET")  &&  !method.equals("HEAD")) {
                if (verbosity > 0) {
                    System.out.println("501 Not Implemented : " + method + " method.");
                }

                // return the not supported file to the client
                File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                int fileLength = (int) file.length();
                String contentMimeType = "text/html";
                //read content to return to client
                byte[] fileData = readFileData(file, fileLength);

                // send HTTP Headers with data to client
                out.println("HTTP/1.1 501 Not Implemented");
                out.println("Server: Internal HTTP System : 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + contentMimeType);
                out.println("Content-length: " + fileLength);
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer
                // file
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

            } else {
                // GET or HEAD method
                /*if (fileRequested.endsWith("/")) {
                    fileRequested += DEFAULT_FILE;
                }*/

                //File file = new File(WEB_ROOT, fileRequested);
                //int fileLength = (int) file.length();
                //String content = getContentType(fileRequested);

                String testContent = "TEST";

                if (method.equals("GET")) { // GET method so return content
                    //byte[] fileData = readFileData(file, fileLength);

                    if(fileRequested.contains("favicon.ico")) {
                        out.print(new RequestReturn(ContentType.PLAIN_TEXT, "There is no favicon for this site.", ReturnStatus.BAD_REQUEST));
                        System.out.println("Refused a favicon request.");
                    }

                    // send HTTP Headers
                    else {
                        Map<String, String> query;
                        int queryBeginIndex = fileRequested.indexOf("?");
                        switch(fileRequested.substring(0, queryBeginIndex == -1 ? fileRequested.length(): queryBeginIndex)) {
                            case "/sendQuery":
                                System.out.println("Endpoint /sendQuery requested.");
                                RequestReturn content = new RequestReturn(ContentType.HTML, sendQueryPage, ReturnStatus.OK);
                                out.print(content);
                                if (verbosity > 1) {
                                    System.out.println("File " + fileRequested + " returned with status 200 OK:" + "\n " + content);
                                }
                                break;
                            case "/listen":
                                System.out.println("Endpoint /listen requested.");
                                RequestReturn page;
                                if(fileRequested.contains("?")) {
                                    query = splitQuery(fileRequested);
                                    System.out.println("Query: \n" + query);
                                    try {
                                        String question = query.get("query");
                                        System.out.println("Question: \n" + question);
                                        page = new RequestReturn(ContentType.HTML, resultPage.replace("INPUT RESULT HERE", QueryHandler.handleQuery(question).replace("\"", "\\\"" )), ReturnStatus.UNAVAILABLE);
                                    } catch(ArrayIndexOutOfBoundsException e) {
                                        page = new RequestReturn(ContentType.PLAIN_TEXT, "At least one of your queries didn't return any results. Try rephrasing them.", ReturnStatus.BAD_REQUEST);
                                    }


                                    out.print(page);
                                } else {
                                    System.out.println("No query was given for the /listen endpoint.");
                                    page = new RequestReturn(ContentType.HTML, sendQueryPage, ReturnStatus.OK);

                                    out.print(page);
                                }

                                if (verbosity > 1) {
                                    System.out.println("File " + fileRequested + " returned with status 200 OK:" + "\n " + page);
                                }
                                break;
                            default:
                                System.out.println("Endpoint default requested.");
                                RequestReturn returnContent = new RequestReturn(ContentType.HTML, sendQueryPage, ReturnStatus.OK);
                                out.print(returnContent);
                                if (verbosity > 1) {
                                    System.out.println("File " + fileRequested + " returned with status 200 OK:" + "\n " + returnContent);
                                }
                                break;
                        }

                    }
                    out.flush(); // flush character output stream buffer

                    //dataOut.write(fileData, 0, fileLength);
                    //dataOut.flush();
                }


            }

        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close(); // close socket connection
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (verbosity > 0) {
                System.out.println("Connection closed.\n");
            }
        }


    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }


    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: Internal HTTP System : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbosity > 0) {
            System.out.println("File " + fileRequested + " not found");
        }
    }

    public static Map<String, String> splitQuery(String endpoint) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String query = endpoint.substring(endpoint.indexOf('?') + 1);
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}