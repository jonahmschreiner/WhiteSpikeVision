package com.whitespike.visionstructure;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import com.whitespike.vision.*;

/**
 * Represents the top level structure of the api, a digital representation of an environment generated by the raw data received.
 * @author Jonah Schreiner
 *
 */
public class Env {
		/**
		 * The AbstractEnv object which represents the abstract information about the environment (senses, etc.)
		 */
		public AbstractEnv abstractEnv = new AbstractEnv();
		/**
		 * The RawEnv object which holds the raw data received.
		 */
		public RawEnv rawEnv = new RawEnv();
		/**
		 * The Util object that can be used to obtain raw data streams and perform actions.
		 */
		public Util util = new Util();
		/**
		 * The id of this env object in the database (currently not used in this release).
		 */
		public int dbId;
		/**
		 * Subimage boundaries if a subimage env was created.
		 */
		public Rectangle subImage = null;
		/**
		 * Constructor which allows the user to determine which types of senses are found.
		 * @param includeHigherSenses Should the api find higher senses (relate senses to those that contain them. Ex: a rectangle and a handle are combined to create a door sense).
		 * @param includeImaginarySenses Should the api find imaginary senses (Ex: Rows, Columns, Arrays of senses/objects).
		 */
		public Env(boolean includeHigherSenses, boolean includeImaginarySenses) {
			senseEnv(includeHigherSenses, includeImaginarySenses);
		}
		/**
		 * Env constructor used for only working on a subsection of the display (usually used for testing/demos).
		 * @param xOffset X pos of the top left coordinate of the subimage (top left of image is 0,0)
		 * @param yOffset Y pos of the top left coordinate of the subimage (top left of image is 0,0)
		 * @param width Width of the subimage.
		 * @param height Height of the subimage.
		 */
		public Env(int xOffset, int yOffset, int width, int height) {
			this.rawEnv = senseRawEnv(this, xOffset, yOffset, width, height);
			this.abstractEnv = senseAbstractEnv(false, false);
		}
		/**
		 * Env constructor used for only working on a subsection of the display (usually used for testing/demos).
		 * Includes the higherSense and imaginarySense finding parameters.
		 * @param xOffset X pos of the top left coordinate of the subimage (top left of image is 0,0)
		 * @param yOffset Y pos of the top left coordinate of the subimage (top left of image is 0,0)
		 * @param width Width of the subimage.
		 * @param height Height of the subimage.
		 * @param includeHigherSenses Should the api find higher senses?
		 * @param includeImaginarySenses Should the api find imaginary senses?
		 */
		public Env(int xOffset, int yOffset, int width, int height, boolean includeHigherSenses, boolean includeImaginarySenses) {
			this.rawEnv = senseRawEnv(this, xOffset, yOffset, width, height);
			this.abstractEnv = senseAbstractEnv(includeHigherSenses, includeImaginarySenses);
		}
		/**
		 * Default constructor that only finds base senses.
		 */
		public Env() {
			senseEnv(false, false);
		}
		/**
		 * Empty constructor (in case the env shouldn't be filled automatically).
		 * @param code
		 */
		public Env(int code) {}
		/**
		 * Constructor where a raw env is supplied.
		 * @param rawEnvIn The raw env to use for abstract env generation.
		 * @param includeHigherSenses Should higher senses be found.
		 * @param includeImaginarySenses Should imaginary senses be found.
		 */
		public Env(RawEnv rawEnvIn, boolean includeHigherSenses, boolean includeImaginarySenses) {
			this.rawEnv = rawEnvIn;
			senseAbstractEnv(includeHigherSenses, includeImaginarySenses);
		}
		/**
		 * Constructor where only a raw env is supplied
		 * @param rawEnvIn The raw env to use for abstract env generation.
		 */
		public Env(RawEnv rawEnvIn) {
			this.rawEnv = rawEnvIn;
			senseAbstractEnv(false, false);
		}
		/**
		 * Constructor where both the raw and abstract env are supplied and no generation is needed.
		 * @param rawEnvIn The raw env to use.
		 * @param abstractEnvIn The abstract env to use.
		 */
		public Env(RawEnv rawEnvIn, AbstractEnv abstractEnvIn) {
			this.rawEnv = rawEnvIn;
			this.abstractEnv = abstractEnvIn;
		}
		/**
		 * Abstraction for the generation of both raw and abstract envs.
		 * @param includeHigherSenses Should higher senses be found.
		 * @param includeImaginarySenses Should imaginary senses be found.
		 */
		public void senseEnv(boolean includeHigherSenses, boolean includeImaginarySenses) {
			this.rawEnv = senseRawEnv(this);
			this.abstractEnv = senseAbstractEnv(includeHigherSenses, includeImaginarySenses);
		}
		/**
		 * Obtain raw info to fill the raw env (currently takes a screenshot, obtains cpu usage, mouse movement, mouse location).
		 * @param envIn The previous env to get mouse change raw info.
		 * @return RawEnv that was created.
		 */
		public RawEnv senseRawEnv(Env envIn) {
			if (envIn.subImage != null) {
				return senseRawEnv(envIn, envIn.subImage.x, envIn.subImage.y, envIn.subImage.width, envIn.subImage.height);
			}
			RawEnv currentRawEnv = new RawEnv();
			currentRawEnv.currentDateTime = LocalDateTime.now();
			try {
				currentRawEnv.currentDisplay = getScreenshot();
			} catch (Exception e) {
				e.printStackTrace();
			}
			currentRawEnv.currentCpuUsage = util.getCurrentCpuUsage();
			currentRawEnv.mouseLocation = MouseInfo.getPointerInfo().getLocation();
			if (currentRawEnv.mouseLocation.x > envIn.rawEnv.mouseLocation.x) {
				currentRawEnv.mouseXChange = 1;
			} else if (currentRawEnv.mouseLocation.x < envIn.rawEnv.mouseLocation.x) {
				currentRawEnv.mouseXChange = -1;
			} else {
				currentRawEnv.mouseXChange = 0;
			}
			
			if (currentRawEnv.mouseLocation.y > envIn.rawEnv.mouseLocation.y) {
				currentRawEnv.mouseYChange = 1;
			} else if (currentRawEnv.mouseLocation.y < envIn.rawEnv.mouseLocation.y) {
				currentRawEnv.mouseYChange = -1;
			} else {
				currentRawEnv.mouseYChange = 0;
			}
			this.rawEnv = currentRawEnv;
			return currentRawEnv;
		}
		/**
		 * Raw env generation for env creation using a subsection of the display.
		 * @param envIn Env to update.
		 * @param x Env image x offset.
		 * @param y Env image y offset (of top left corner of screen).
		 * @param width Env image width.
		 * @param height Env image height.
		 * @return The generated rawEnv.
		 */
		public RawEnv senseRawEnv(Env envIn, int x, int y, int width, int height) {
			RawEnv currentRawEnv = new RawEnv();
			currentRawEnv.currentDateTime = LocalDateTime.now();
			try {
				currentRawEnv.currentDisplay = getScreenshot(x, y, width, height);
			} catch (Exception e) {
				e.printStackTrace();
			}
			currentRawEnv.currentCpuUsage = util.getCurrentCpuUsage();
			currentRawEnv.mouseLocation = MouseInfo.getPointerInfo().getLocation();
			if (currentRawEnv.mouseLocation.x > envIn.rawEnv.mouseLocation.x) {
				currentRawEnv.mouseXChange = 1;
			} else if (currentRawEnv.mouseLocation.x < envIn.rawEnv.mouseLocation.x) {
				currentRawEnv.mouseXChange = -1;
			} else {
				currentRawEnv.mouseXChange = 0;
			}
			
			if (currentRawEnv.mouseLocation.y > envIn.rawEnv.mouseLocation.y) {
				currentRawEnv.mouseYChange = 1;
			} else if (currentRawEnv.mouseLocation.y < envIn.rawEnv.mouseLocation.y) {
				currentRawEnv.mouseYChange = -1;
			} else {
				currentRawEnv.mouseYChange = 0;
			}
			this.rawEnv = currentRawEnv;
			return currentRawEnv;
		}
		/**
		 * Calls RawEnvToAbstractEnv to generate an abstract env from the raw env.
		 * @param includeHigherSenses Should higher senses be found?
		 * @param includeImaginarySenses Should imaginary senses be found?
		 * @return The abstract env it generated from the raw env data.
		 */
		public AbstractEnv senseAbstractEnv(boolean includeHigherSenses, boolean includeImaginarySenses) {
			return RawEnvToAbstractEnv.extract(this.rawEnv, includeHigherSenses, includeImaginarySenses);
		}
		/**
		 * Gets a screenshot of the current screen.
		 * @return The screenshot bufferedimage.
		 * @throws Exception
		 */
		public BufferedImage getScreenshot() throws Exception {
		    Rectangle rec = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		    Robot robot = new Robot();
		    BufferedImage img = robot.createScreenCapture(rec); 
		    return img;
		}
		/**
		 * Gets a subimage screenshot of the current screen.
		 * @param x X coordinate of the top left corner of the screenshot.
		 * @param y Y coordinate of the top left corner of the screenshot.
		 * @param height Height of screenshot.
		 * @param width Width of screenshot.
		 * @return The screenshot bufferedimage.
		 * @throws Exception
		 */
		public BufferedImage getScreenshot(int x, int y, int width, int height) throws Exception {
			Dimension dimension = new Dimension(width, height);
		    Rectangle rec = new Rectangle(dimension);
		    rec.x = x;
		    rec.y = y;
		    this.subImage = rec;
		    Robot robot = new Robot();
		    BufferedImage img = robot.createScreenCapture(rec); 
		    return img;
		}
		/**
		 * Two envs are equal if their abstract envs are.
		 */
		@Override
		public boolean equals(Object o) {
			if (o == this){
				return true;
			}
			if (o instanceof Env){
				Env c = (Env) o;
				boolean output = false;
				if (c.abstractEnv.equals(this.abstractEnv)){ //variables (that describe conditions) are the same
					output = true;
				} else {
					output = false;
				}
				return output;
			} else {
				return false;
			}
		}
}
