package Examples;

import java.awt.Frame;

import javax.swing.JOptionPane;

import com.whitespike.vision.UpdateEnv;
import com.whitespike.visionstructure.Env;
import com.whitespike.visionstructure.Sense;

import test.FillInWholeSense;
import test.VisualOutputOfSensesFromSensesAndImage;
/**
 * Demo of env creation and updating. (Remember that the top left coordinate of the display is 0,0).
 * @author Jonah Schreiner
 */
public class BasicExample {
	/**
	 * Demo of Env Creation
	 * @param args Takes one or five args. The first determines the path of where the output images will be created, the rest are optional and determine the subimage used (to only use a part of the screen for performance reasons). They are xOffset, yOffset, width, height (top left of image is 0,0).
	 */
	public static void main(String[] args) {
		String path = null;
		int xOffset = 605;
		int yOffset = 271;
		int width = 468;
		int height = 368;
		if (args.length == 5) {
			try {
				path = args[0];
				xOffset = Integer.valueOf(args[1]);
				yOffset = Integer.valueOf(args[2]);
				width = Integer.valueOf(args[3]);
				height = Integer.valueOf(args[4]);
			} catch (Exception e) {
				e.printStackTrace();
				xOffset = 605;
				yOffset = 271;
				width = 468;
				height = 398;
				if (path == null) {
					return;
				}
			}
		} else if (args.length == 1){
			path = args[0];
		} else {
			System.out.println("no path argument provided.");
		}
		
		
		
		long envStartTime = System.currentTimeMillis();
		
		//Only line needed to create an env. Use: Env env = new Env(); to do the whole screen (will take awhile)
		Env env = new Env(xOffset, yOffset, width, height);
		//
		
		
		
		long envEndTime = System.currentTimeMillis();
		System.out.println("Env Creation Time: " + (envEndTime - envStartTime) + " milliseconds");
		
		VisualOutputOfSensesFromSensesAndImage.execute(env.abstractEnv.senses, env.rawEnv.currentDisplay, "initialEnv", path);
		for (int i = 0; i < env.abstractEnv.senses.size(); i++) {
			Sense currSense = env.abstractEnv.senses.get(i);
			System.out.println("Sense at index " + i + ":");
			System.out.println("Height: " + currSense.orientation.height + " Width: " + currSense.orientation.width);
			System.out.println("Rotation: " + currSense.orientation.rotation + " Color: (" + currSense.orientation.color.getRed() + ", " + currSense.orientation.color.getGreen() + ", " + currSense.orientation.color.getBlue() + ")");
			System.out.println("Position: (" + currSense.orientation.position.x + ", " + currSense.orientation.position.y + ")");
			System.out.println("Definition: \"" + currSense.definition.toString() + "\"");
			System.out.println("\n");
			if (i > 30) {
				break;
			}
		}
		
		System.out.println("\n---------------\n");
		
		//Create popup
		try {
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, "Finished env creation. You have three seconds to change the env after clicking okay before the api processes changes.");
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		envStartTime = System.currentTimeMillis();
		
		//Only line needed to update an env. (automatically only takes a screenshot of the same size as the env was created with)
		UpdateEnv.update(env);
		//
		
		envEndTime = System.currentTimeMillis();
		System.out.println("Env Updating Time: " + (envEndTime - envStartTime) + " milliseconds");
		
		VisualOutputOfSensesFromSensesAndImage.execute(env.abstractEnv.senses, env.rawEnv.currentDisplay, "updatedEnv", path);
		FillInWholeSense.exec(env.abstractEnv.senses.get(0).blob.pixels, env.rawEnv.currentDisplay, "senseAtIndex0FilledIn", path);
		
		//Write sense info to console
		for (int i = 0; i < env.abstractEnv.senses.size(); i++) {
			Sense currSense = env.abstractEnv.senses.get(i);
			System.out.println("Sense at index " + i + ":");
			System.out.println("Height: " + currSense.orientation.height + " Width: " + currSense.orientation.width);
			System.out.println("Rotation: " + currSense.orientation.rotation + " Color: (" + currSense.orientation.color.getRed() + ", " + currSense.orientation.color.getGreen() + ", " + currSense.orientation.color.getBlue() + ")");
			System.out.println("Position: (" + currSense.orientation.position.x + ", " + currSense.orientation.position.y + ")");
			System.out.println("\n");
		}
		
		//Create popup
		try {
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, "Finished env updating.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
}
