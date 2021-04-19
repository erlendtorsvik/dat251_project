package hvl.no.dat251.group3project.firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;

@Service
public class FBInitialize {
	StorageOptions storageOptions;
	String projectId = "dat251-project";
	String bucketName = "dat251-project.appspot.com";

	@PostConstruct
	public void initialize() {
		// Use the application default credentials
		try {
			InputStream serviceAccount = new FileInputStream(
					"src/main/resources/dat251-project-firebase-adminsdk-5rep5-58f8e1fbc8.json");
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials)
					.setStorageBucket(bucketName).build();
			this.storageOptions = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(credentials)
					.build();
			if (FirebaseApp.getApps().isEmpty())
				FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Firestore getFirebase() {
		return FirestoreClient.getFirestore();
	}

	public Bucket getBucket() {
		return StorageClient.getInstance().bucket();
	}

	public String uploadFile(MultipartFile multipartFile) {
		try {
			String fileName = multipartFile.getOriginalFilename();
			fileName = UUID.randomUUID().toString().concat(getExtension(fileName));
			File file = convertToFile(multipartFile, fileName);

			BlobId blobId = BlobId.of(bucketName, fileName);
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
			Storage storage = storageOptions.getService();
			storage.create(blobInfo, Files.readAllBytes(file.toPath()));

			file.delete();
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
			return "null";
		}
	}

	private String getExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
		File tempFile = new File(fileName);
		try (FileOutputStream fos = new FileOutputStream(tempFile)) {
			fos.write(multipartFile.getBytes());
			fos.close();
		}
		return tempFile;
	}

	@Value("${app.upload.dir}")
	public String uploadDir;

	public boolean downloadFile(String fileName) throws IOException {
		String destFilePath = uploadDir + fileName;

		Storage storage = storageOptions.getService();
		Blob blob = storage.get(BlobId.of(bucketName, fileName));
		blob.downloadTo(Paths.get(destFilePath));
		return true;
	}

	public void deleteFile(String fileName) {
		Storage storage = storageOptions.getService();

		BlobId blobId = BlobId.of(bucketName, fileName);
		storage.delete(blobId);
	}
}