/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package server;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        //System.out.println(new App().getGreeting());
        // Set the environment variable with the following command in the console: $env:GOOGLE_APPLICATION_CREDENTIALS="path/to/the/service-account-file.json"
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .setDatabaseUrl("https://devlink-89dfe.firebaseio.com/")
            .build();

        FirebaseApp.initializeApp(options);
    }
}