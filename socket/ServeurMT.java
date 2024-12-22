import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Vector;

public class ServeurMT extends Thread {
    private final static String  fichierConf="C:/Users/Mamy Tsiferana/Documents/ITU_L2/reseaux/Socket/conf.txt";
    static String repertoire ;
    static int PORT;
    static String php;
    boolean running=true;

    public void recupererFichierConf(){
        try {
            File file=new File(fichierConf);
            BufferedReader br=new BufferedReader(new FileReader(file));
            String line;
            while((line=br.readLine())!=null){
                System.out.println(line);
                if (line.startsWith("PORT=")) {
                    String[] port=line.split("=");
                    PORT=Integer.parseInt(port[1]);
                }
                else if (line.startsWith("REPERTOIRE=")) {
                    String[] rep=line.split("=");
                    repertoire=rep[1];
                }
                else if (line.startsWith("PHP=")) {
                    String[] rep=line.split("=");
                    php=rep[1];
                }
                
            }
            br.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void stopServeur(){
        running=false;
        this.interrupt();
        System.out.println("Serveur arrêté");
    }
    @Override
    public void run() {
        recupererFichierConf();
        try (ServerSocket serveur = new ServerSocket(PORT)) {
            System.out.println("Serveur attend la connexion");

            while (running) {
                Socket s = serveur.accept();
                System.out.println("Connexion réussie avec l'IP : " + s.getRemoteSocketAddress().toString());
                new Conversation(s).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Conversation extends Thread {
        Socket s;

        public Conversation(Socket s) {
            this.s = s;
        }

        @Override
        public void run() {
            try (InputStream is = s.getInputStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(is));
                 PrintWriter pw = new PrintWriter(s.getOutputStream(), true)) {

                String requete = br.readLine();
                System.out.println("Requete: " + requete);

                if (requete == null || requete.isEmpty()) {
                    System.out.println("Connexion fermée par le client");
                    return;
                }

                String[] div = requete.split(" ");
                if (div.length > 2) {
                    System.out.println(div[1]);
                    String chemin = URLDecoder.decode(div[1], StandardCharsets.UTF_8.toString());
                    System.out.println(chemin);
                    if (chemin.equals("/")) chemin = "";
                    String[] vraiChemin=chemin.split("\\?");
                    File file = new File(repertoire + vraiChemin[0]);

                    if (div[0].equalsIgnoreCase("GET")) {
                        if (file.exists()) {
                            if (file.isDirectory()) {
                                directory(s, file);
                            } else {
                                if(file.getName().endsWith(".php")){
                                    //sendPhp(s, file);
                                    if(php.equals("oui")){
                                        sendPhpOutput(pw, file, chemin, "GET", null);
                                    }
                                    else{
                                        sendHttpResponse(s, 1234, "Le code php n'est pas compatible avec le serveur");
                                        return ;
                                    }
                                }
                                else{
                                    sendFile(s, file);
                                }
                            }
                        } else {
                            sendHttpResponse(s, 404, "Fichier non trouvé");
                        }
                    } else if (div[0].equalsIgnoreCase("POST")) {
                        if (file.exists()) {
                            String line;
                            int contentLength = 0;
                            while ((line = br.readLine()) != null && !line.isEmpty()) {
                                System.out.println(line);
                                if (line.startsWith("Content-Length:")) {
                                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                                }
                            }
                
                            StringBuilder body=new StringBuilder();
                            if (contentLength > 0) {
                                char[] buffer = new char[contentLength];
                                int bytesRead = br.read(buffer, 0, contentLength);
                                body.append(buffer, 0, bytesRead);
                            }
                            System.out.println(body.toString());
                            sendPhpOutput(pw, file, chemin, "POST", body.toString());

                            //sendPhpResponse(s,file,"POST",null,contentLength);
                            //sendFilePost(s, file, br);
                        } else {
                            sendHttpResponse(s, 404, "Fichier non trouvé");
                        }
                    } else {
                        sendHttpResponse(s, 405, "Méthode non autorisée");
                    }
                } else {
                    sendHttpResponse(s, 400, "Requête invalide");
                }

            } catch (Exception e) {
                e.printStackTrace();
                sendHttpResponse(s, 500, "Erreur interne du serveur");
            } finally {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendHttpResponse(Socket s, int statusCode, String message) {
        try (PrintWriter pw = new PrintWriter(s.getOutputStream(), true)) {
            String statusMessage;
            switch (statusCode) {
                case 200 -> statusMessage = "OK";
                case 400 -> statusMessage = "Bad Request";
                case 404 -> statusMessage = "Not Found";
                case 405 -> statusMessage = "Method Not Allowed";
                case 500 -> statusMessage = "Internal Server Error";
                default -> statusMessage = "Unknown Status";
            }

            pw.println("HTTP/1.1 " + statusCode + " " + statusMessage);
            pw.println("Content-Type: text/html; charset=UTF-8");
            pw.println();
            pw.println("<html><body>");
            pw.println("<h1>" + statusCode + " - " + statusMessage + "</h1>");
            pw.println("<p>" + message + "</p>");
            pw.println("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void directory(Socket s, File file) {
        try (PrintWriter pw = new PrintWriter(s.getOutputStream(), true)) {
            // Réponse pour un répertoire
            for (File sousfile : file.listFiles()) {
                if(sousfile.getName().startsWith("index") && !sousfile.isDirectory()){
                    sendFile(s,sousfile);
                    return;
                }
            }
            pw.println("HTTP/1.1 200 OK");
            pw.println("Content-Type: text/html; charset=UTF-8");
            pw.println();

            pw.println("<html><head>");
            pw.println("<title>Gestion des Requêtes HTTP</title>");
            pw.println("</head><body>");

            pw.println("<main>");

           
            pw.println("<h1>Contenu du dossier : " + file.getName() + "</h1>");
            
            pw.println("<section>");
            pw.println("<h2>Fichiers Disponibles</h2>");
            pw.println("<ul>");

            
            for (File sousfile : file.listFiles()) {
                
                String cheminRelatif = "/"+sousfile.getName();
                String chemin=file.getAbsolutePath().replace("\\", "/")+cheminRelatif;
                String cheminRepertoire = chemin.substring(chemin.indexOf("www") + 3); // +4 pour ignorer "www/"
                cheminRepertoire=cheminRepertoire.replaceAll(" ","%20");
                pw.println("<a href=" + cheminRepertoire +"><li>" + sousfile.getName() + "</li></a>");
                System.out.println(cheminRepertoire);
            }

            pw.println("</ul>");
            pw.println("</section>");
            pw.println("</main>");

            pw.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFile(Socket s, File file) {
        try (OutputStream os = s.getOutputStream(); PrintWriter pw = new PrintWriter(os, true)) {
            String contentType = Files.probeContentType(file.toPath());
            byte[] fileBytes = Files.readAllBytes(file.toPath());

            pw.println("HTTP/1.1 200 OK");
            pw.println("Content-Type: " + contentType + "; charset=UTF-8");
            pw.println();
            os.write(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String phpOutput(String requestedFile, String method, String postData) throws IOException {
        Vector<String> commands = new Vector<>();
        commands.add("php");
        commands.add("-r");
        ProcessBuilder processBuilder;
        StringBuilder output = new StringBuilder();

        //Pour la methode GET
        if (method.equals("GET")) {
            System.out.println("methode GET");
            String[] parts = requestedFile.split("\\?");
            System.out.println("requested File:"+requestedFile);
            String filePath = parts[0]; //c'est le fichier (ex: /form.php)
            String params = parts.length > 1 ? parts[1] : ""; //raha inf a 1 tsisy parametre
            System.out.println("size :"+parts.length);
            if(parts.length > 1) {
                System.out.println("parts[1]:" +parts[1]);
            } 

            if(filePath.equals("/")) {
                filePath = "/index.php";
            }
            System.out.println("File path: "+filePath);
            System.out.println("Params : "+params);

            // commande pour executer PHP en mode GET
            commands.add("parse_str('" + params + "', $_GET); include('" +"./www" + filePath + "');");
            processBuilder = new ProcessBuilder(commands);
            processBuilder.redirectErrorStream(true);

            // Lancer le processus PHP
            Process process = processBuilder.start();

            // Lire la sortie du processus PHP
            try (BufferedReader phpOutput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = phpOutput.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
        }

        //Pour la methode POST
        else if (method.equals("POST")) {
            // commande pour executer PHP en mode POST
            commands.add("parse_str('" + postData + "', $_POST); include('" +"./www" + requestedFile + "');");

            processBuilder = new ProcessBuilder(commands);
            processBuilder.redirectErrorStream(true);

            // Lancer le processus PHP
            Process process = processBuilder.start();

            // Écrire les données POST dans l'entrée standard du processus PHP
            try (OutputStream outputStream = process.getOutputStream()) {
                outputStream.write(postData.getBytes());
                outputStream.flush();
            }

            try (BufferedReader phpOutput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = phpOutput.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
        }

        System.out.println(output.toString());
        return output.toString();
    }

    public static void sendPhpOutput(PrintWriter out, File file, String requestedFile, String method, String postData) throws IOException {
        //Hiexecutena anle fichier PHP
        //file.getAbsolutePath(): chemin complet du fichier php
        ProcessBuilder processBuilder = new ProcessBuilder("php", file.getAbsolutePath());
        Process process = processBuilder.start();

        BufferedReader phpError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println();

        out.println(phpOutput(requestedFile, method, postData));
        phpError.close();
        out.flush();
    }
}
