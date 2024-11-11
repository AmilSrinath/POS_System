package pos.system.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;

/**
 * @author Amil Srinath
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Session session;
        try {
            session = FactoryConfiguration.getInstance().getSession();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Transaction transaction = session.beginTransaction();
        transaction.commit();
        session.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("POS System");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}