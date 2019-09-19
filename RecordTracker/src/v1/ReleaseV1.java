package v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
/*
 * AUTHOR: Taylor Gutzmann August 2019
 * Public release version: 1.0
 */

//Win on left, Draw in middle, Loss on right

public class ReleaseV1 extends Application{
	final String appName = "Win/Draw/Loss Tracker v1";
	final static double FPS = 4.0; // frames per second
	static double WIDTH = 500;
	static double HEIGHT = 200;
	static int wins, losses, draws;
	File file = new File("Record.txt");
	static File input = new File("Inputs.txt");
	static String text;
	FileOutputStream os;
	static FileOutputStream osI;
	static FileInputStream isI;
	static Scanner sI;
	PrintWriter pw;
	static PrintWriter pwI;
	static boolean inclDraw;
	
public static void main(String[] args) {
	if(input.exists()) {
		read();
		launch(args);
	} else {
		try {
			osI = new FileOutputStream(input);
			pwI = new PrintWriter(osI);
			pwI.print("0,0,0,true");
			pwI.flush();
			pwI.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		main(args);
	}
	}
	
void setHandlers(Scene scene) {
		scene.setOnKeyPressed(
				e -> {
					KeyCode button = e.getCode();
					switch(button){
						case UP: 	wins++;
									text = grabText();	
							break;
						case DOWN: 	losses++;
									text = grabText();
							break;
						case LEFT: 	draws++;
									text = grabText();
							break;
						case RIGHT:	wins = 0;
									draws = 0;
									losses = 0;
									text = grabText();
							break;
						case D:		if (inclDraw) {
										inclDraw = false;
										break;
									}
									inclDraw = true;
							break;
						default:	break;
					}
					
				});
}

public void update() {
	try {
		os = new FileOutputStream(file);
		pw = new PrintWriter(os);
		pw.print(text);
		pw.flush();
		pw.close();
		osI = new FileOutputStream(input);
		pwI = new PrintWriter(osI);
		pwI.print(wins+","+draws+","+losses+","+inclDraw);
		pwI.flush();
		pwI.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}

}

void render(GraphicsContext gc) {
	gc.setFill(Color.BLACK);
	gc.fillRect(0, 0, WIDTH, HEIGHT);
	gc.setFill(Color.WHITE);
	gc.fillText(grabText(), 200, 100);
	gc.fillText("Made by: DSkid48", 402, 198);
}

static void read() {
	try {
		isI = new FileInputStream(input);
		sI = new Scanner(isI);
		sI.useDelimiter(",");
		wins = Integer.parseInt(sI.next());
		draws = Integer.parseInt(sI.next());
		losses = Integer.parseInt(sI.next());
		inclDraw = Boolean.parseBoolean(sI.next());
		sI.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	
	text = grabText();
}
	
static String grabText() {
	if (inclDraw)
		return "Today's Record: "+wins+"W-"+draws+"D-"+losses+"L";
	return "Today's Record: "+wins+"W-"+losses+"L";
	
}

public void start(Stage theStage) {
		theStage.setTitle(appName);

		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);

		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Initial setup
		setHandlers(theScene);

		// Setup and start animation loop (Timeline)
		KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS),
				e -> {
					// update position
					update();
					// draw frame
					render(gc);
				}
				);
		Timeline mainLoop = new Timeline(kf);
		mainLoop.setCycleCount(Animation.INDEFINITE);
		mainLoop.play();


		theStage.show();
}
}