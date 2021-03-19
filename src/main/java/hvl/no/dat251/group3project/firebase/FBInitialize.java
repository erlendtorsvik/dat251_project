package hvl.no.dat251.group3project.firebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class FBInitialize {

	@PostConstruct
	public void initialize() {
		// Use the application default credentials
		try {
			InputStream serviceAccount = new FileInputStream(
					"src/main/resources/dat251-project-firebase-adminsdk-5rep5-58f8e1fbc8.json");
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
			if (FirebaseApp.getApps().isEmpty())
				FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Firestore getFirebase() {
		return FirestoreClient.getFirestore();
	}
}