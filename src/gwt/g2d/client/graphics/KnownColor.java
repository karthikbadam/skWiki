/*
 * Copyright 2009 Hao Nguyen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwt.g2d.client.graphics;

import gwt.g2d.shared.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Contains a set of known colors.
 * 
 * @see <a href="http://www.w3schools.com/css/css_colornames.asp">
 * http://www.w3schools.com/css/css_colornames.asp</a>
 * 
 * @author hao1300@gmail.com
 */
public final class KnownColor extends Color {
	private static final List<KnownColor> KNOWN_COLORS = new ArrayList<KnownColor>();
	
	public static final Color ALICE_BLUE = new KnownColor("AliceBlue", 240, 248, 255);
	public static final Color ANTIQUE_WHITE = new KnownColor("AntiqueWhite", 250, 235, 215);
	public static final Color AQUA = new KnownColor("Aqua", 0, 255, 255);
	public static final Color AQUAMARINE = new KnownColor("Aquamarine", 127, 255, 212);
	public static final Color AZURE = new KnownColor("Azure", 240, 255, 255);
	public static final Color BEIGE = new KnownColor("Beige", 245, 245, 220);
	public static final Color BISQUE = new KnownColor("Bisque", 255, 228, 196);
	public static final Color BLACK = new KnownColor("Black", 0, 0, 0);
	public static final Color BLANCHED_ALMOND = new KnownColor("BlanchedAlmond", 255, 235, 205);
	public static final Color BLUE = new KnownColor("Blue", 0, 0, 255);
	public static final Color BLUE_VIOLET = new KnownColor("BlueViolet", 138, 43, 226);
	public static final Color BROWN = new KnownColor("Brown", 165, 42, 42);
	public static final Color BURLY_WOOD = new KnownColor("BurlyWood", 222, 184, 135);
	public static final Color CADET_BLUE = new KnownColor("CadetBlue", 95, 158, 160);
	public static final Color CHARTREUSE = new KnownColor("Chartreuse", 127, 255, 0);
	public static final Color CHOCOLATE = new KnownColor("Chocolate", 210, 105, 30);
	public static final Color CORAL = new KnownColor("Coral", 255, 127, 80);
	public static final Color CORNFLOWER_BLUE = new KnownColor("CornflowerBlue", 100, 149, 237);
	public static final Color CORNSILK = new KnownColor("Cornsilk", 255, 248, 220);
	public static final Color CRIMSON = new KnownColor("Crimson", 220, 20, 60);
	public static final Color CYAN = new KnownColor("Cyan", 0, 255, 255);
	public static final Color DARK_BLUE = new KnownColor("DarkBlue", 0, 0, 139);
	public static final Color DARK_CYAN = new KnownColor("DarkCyan", 0, 139, 139);
	public static final Color DARK_GOLDEN_ROD = new KnownColor("DarkGoldenRod", 184, 134, 11);
	public static final Color DARK_GRAY = new KnownColor("DarkGray", 169, 169, 169);
	public static final Color DARK_GREEN = new KnownColor("DarkGreen", 0, 100, 0);
	public static final Color DARK_KHAKI = new KnownColor("DarkKhaki", 189, 183, 107);
	public static final Color DARK_MAGENTA = new KnownColor("DarkMagenta", 139, 0, 139);
	public static final Color DARK_OLIVE_GREEN = new KnownColor("DarkOliveGreen", 85, 107, 47);
	public static final Color DARKORANGE = new KnownColor("Darkorange", 255, 140, 0);
	public static final Color DARK_ORCHID = new KnownColor("DarkOrchid", 153, 50, 204);
	public static final Color DARK_RED = new KnownColor("DarkRed", 139, 0, 0);
	public static final Color DARK_SALMON = new KnownColor("DarkSalmon", 233, 150, 122);
	public static final Color DARK_SEA_GREEN = new KnownColor("DarkSeaGreen", 143, 188, 143);
	public static final Color DARK_SLATE_BLUE = new KnownColor("DarkSlateBlue", 72, 61, 139);
	public static final Color DARK_SLATE_GRAY = new KnownColor("DarkSlateGray", 47, 79, 79);
	public static final Color DARK_TURQUOISE = new KnownColor("DarkTurquoise", 0, 206, 209);
	public static final Color DARK_VIOLET = new KnownColor("DarkViolet", 148, 0, 211);
	public static final Color DEEP_PINK = new KnownColor("DeepPink", 255, 20, 147);
	public static final Color DEEP_SKY_BLUE = new KnownColor("DeepSkyBlue", 0, 191, 255);
	public static final Color DIM_GRAY = new KnownColor("DimGray", 105, 105, 105);
	public static final Color DODGER_BLUE = new KnownColor("DodgerBlue", 30, 144, 255);
	public static final Color FIRE_BRICK = new KnownColor("FireBrick", 178, 34, 34);
	public static final Color FLORAL_WHITE = new KnownColor("FloralWhite", 255, 250, 240);
	public static final Color FOREST_GREEN = new KnownColor("ForestGreen", 34, 139, 34);
	public static final Color FUCHSIA = new KnownColor("Fuchsia", 255, 0, 255);
	public static final Color GAINSBORO = new KnownColor("Gainsboro", 220, 220, 220);
	public static final Color GHOST_WHITE = new KnownColor("GhostWhite", 248, 248, 255);
	public static final Color GOLD = new KnownColor("Gold", 255, 215, 0);
	public static final Color GOLDEN_ROD = new KnownColor("GoldenRod", 218, 165, 32);
	public static final Color GRAY = new KnownColor("Gray", 128, 128, 128);
	public static final Color GREEN = new KnownColor("Green", 0, 128, 0);
	public static final Color GREEN_YELLOW = new KnownColor("GreenYellow", 173, 255, 47);
	public static final Color HONEY_DEW = new KnownColor("HoneyDew", 240, 255, 240);
	public static final Color HOT_PINK = new KnownColor("HotPink", 255, 105, 180);
	public static final Color INDIAN_RED = new KnownColor("IndianRed", 205, 92, 92);
	public static final Color INDIGO = new KnownColor("Indigo", 75, 0, 130);
	public static final Color IVORY = new KnownColor("Ivory", 255, 255, 240);
	public static final Color KHAKI = new KnownColor("Khaki", 240, 230, 140);
	public static final Color LAVENDER = new KnownColor("Lavender", 230, 230, 250);
	public static final Color LAVENDER_BLUSH = new KnownColor("LavenderBlush", 255, 240, 245);
	public static final Color LAWN_GREEN = new KnownColor("LawnGreen", 124, 252, 0);
	public static final Color LEMON_CHIFFON = new KnownColor("LemonChiffon", 255, 250, 205);
	public static final Color LIGHT_BLUE = new KnownColor("LightBlue", 173, 216, 230);
	public static final Color LIGHT_CORAL = new KnownColor("LightCoral", 240, 128, 128);
	public static final Color LIGHT_CYAN = new KnownColor("LightCyan", 224, 255, 255);
	public static final Color LIGHT_GOLDEN_ROD_YELLOW = new KnownColor("LightGoldenRodYellow", 250, 250, 210);
	public static final Color LIGHT_GREY = new KnownColor("LightGrey", 211, 211, 211);
	public static final Color LIGHT_GREEN = new KnownColor("LightGreen", 144, 238, 144);
	public static final Color LIGHT_PINK = new KnownColor("LightPink", 255, 182, 193);
	public static final Color LIGHT_SALMON = new KnownColor("LightSalmon", 255, 160, 122);
	public static final Color LIGHT_SEA_GREEN = new KnownColor("LightSeaGreen", 32, 178, 170);
	public static final Color LIGHT_SKY_BLUE = new KnownColor("LightSkyBlue", 135, 206, 250);
	public static final Color LIGHT_SLATE_GRAY = new KnownColor("LightSlateGray", 119, 136, 153);
	public static final Color LIGHT_STEEL_BLUE = new KnownColor("LightSteelBlue", 176, 196, 222);
	public static final Color LIGHT_YELLOW = new KnownColor("LightYellow", 255, 255, 224);
	public static final Color LIME = new KnownColor("Lime", 0, 255, 0);
	public static final Color LIME_GREEN = new KnownColor("LimeGreen", 50, 205, 50);
	public static final Color LINEN = new KnownColor("Linen", 250, 240, 230);
	public static final Color MAGENTA = new KnownColor("Magenta", 255, 0, 255);
	public static final Color MAROON = new KnownColor("Maroon", 128, 0, 0);
	public static final Color MEDIUM_AQUA_MARINE = new KnownColor("MediumAquaMarine", 102, 205, 170);
	public static final Color MEDIUM_BLUE = new KnownColor("MediumBlue", 0, 0, 205);
	public static final Color MEDIUM_ORCHID = new KnownColor("MediumOrchid", 186, 85, 211);
	public static final Color MEDIUM_PURPLE = new KnownColor("MediumPurple", 147, 112, 216);
	public static final Color MEDIUM_SEA_GREEN = new KnownColor("MediumSeaGreen", 60, 179, 113);
	public static final Color MEDIUM_SLATE_BLUE = new KnownColor("MediumSlateBlue", 123, 104, 238);
	public static final Color MEDIUM_SPRING_GREEN = new KnownColor("MediumSpringGreen", 0, 250, 154);
	public static final Color MEDIUM_TURQUOISE = new KnownColor("MediumTurquoise", 72, 209, 204);
	public static final Color MEDIUM_VIOLET_RED = new KnownColor("MediumVioletRed", 199, 21, 133);
	public static final Color MIDNIGHT_BLUE = new KnownColor("MidnightBlue", 25, 25, 112);
	public static final Color MINT_CREAM = new KnownColor("MintCream", 245, 255, 250);
	public static final Color MISTY_ROSE = new KnownColor("MistyRose", 255, 228, 225);
	public static final Color MOCCASIN = new KnownColor("Moccasin", 255, 228, 181);
	public static final Color NAVAJO_WHITE = new KnownColor("NavajoWhite", 255, 222, 173);
	public static final Color NAVY = new KnownColor("Navy", 0, 0, 128);
	public static final Color OLD_LACE = new KnownColor("OldLace", 253, 245, 230);
	public static final Color OLIVE = new KnownColor("Olive", 128, 128, 0);
	public static final Color OLIVE_DRAB = new KnownColor("OliveDrab", 107, 142, 35);
	public static final Color ORANGE = new KnownColor("Orange", 255, 165, 0);
	public static final Color ORANGE_RED = new KnownColor("OrangeRed", 255, 69, 0);
	public static final Color ORCHID = new KnownColor("Orchid", 218, 112, 214);
	public static final Color PALE_GOLDEN_ROD = new KnownColor("PaleGoldenRod", 238, 232, 170);
	public static final Color PALE_GREEN = new KnownColor("PaleGreen", 152, 251, 152);
	public static final Color PALE_TURQUOISE = new KnownColor("PaleTurquoise", 175, 238, 238);
	public static final Color PALE_VIOLET_RED = new KnownColor("PaleVioletRed", 216, 112, 147);
	public static final Color PAPAYA_WHIP = new KnownColor("PapayaWhip", 255, 239, 213);
	public static final Color PEACH_PUFF = new KnownColor("PeachPuff", 255, 218, 185);
	public static final Color PERU = new KnownColor("Peru", 205, 133, 63);
	public static final Color PINK = new KnownColor("Pink", 255, 192, 203);
	public static final Color PLUM = new KnownColor("Plum", 221, 160, 221);
	public static final Color POWDER_BLUE = new KnownColor("PowderBlue", 176, 224, 230);
	public static final Color PURPLE = new KnownColor("Purple", 128, 0, 128);
	public static final Color RED = new KnownColor("Red", 255, 0, 0);
	public static final Color ROSY_BROWN = new KnownColor("RosyBrown", 188, 143, 143);
	public static final Color ROYAL_BLUE = new KnownColor("RoyalBlue", 65, 105, 225);
	public static final Color SADDLE_BROWN = new KnownColor("SaddleBrown", 139, 69, 19);
	public static final Color SALMON = new KnownColor("Salmon", 250, 128, 114);
	public static final Color SANDY_BROWN = new KnownColor("SandyBrown", 244, 164, 96);
	public static final Color SEA_GREEN = new KnownColor("SeaGreen", 46, 139, 87);
	public static final Color SEA_SHELL = new KnownColor("SeaShell", 255, 245, 238);
	public static final Color SIENNA = new KnownColor("Sienna", 160, 82, 45);
	public static final Color SILVER = new KnownColor("Silver", 192, 192, 192);
	public static final Color SKY_BLUE = new KnownColor("SkyBlue", 135, 206, 235);
	public static final Color SLATE_BLUE = new KnownColor("SlateBlue", 106, 90, 205);
	public static final Color SLATE_GRAY = new KnownColor("SlateGray", 112, 128, 144);
	public static final Color SNOW = new KnownColor("Snow", 255, 250, 250);
	public static final Color SPRING_GREEN = new KnownColor("SpringGreen", 0, 255, 127);
	public static final Color STEEL_BLUE = new KnownColor("SteelBlue", 70, 130, 180);
	public static final Color TAN = new KnownColor("Tan", 210, 180, 140);
	public static final Color TEAL = new KnownColor("Teal", 0, 128, 128);
	public static final Color THISTLE = new KnownColor("Thistle", 216, 191, 216);
	public static final Color TOMATO = new KnownColor("Tomato", 255, 99, 71);
	public static final Color TURQUOISE = new KnownColor("Turquoise", 64, 224, 208);
	public static final Color VIOLET = new KnownColor("Violet", 238, 130, 238);
	public static final Color WHEAT = new KnownColor("Wheat", 245, 222, 179);
	public static final Color WHITE = new KnownColor("White", 255, 255, 255);
	public static final Color WHITE_SMOKE = new KnownColor("WhiteSmoke", 245, 245, 245);
	public static final Color YELLOW = new KnownColor("Yellow", 255, 255, 0);
	public static final Color YELLOW_GREEN = new KnownColor("YellowGreen", 154, 205, 50);

	public static final Color TRANSPARENT = new KnownColor("", 0, 0, 0, 0.0);

	private static final long serialVersionUID = -6337746115562976618L;
		
	private KnownColor(String colorCode, int red, int blue, int green) {
		super(red, blue, green);
		KNOWN_COLORS.add(this);
	}
	
	private KnownColor(String colorCode, int red, int blue, int green, double alpha) {
		super(red, blue, green, alpha);
		KNOWN_COLORS.add(this);
	}
	
	/**
	 * Gets a collection of known colors.
	 */
	public static Collection<KnownColor> getKnownColors() {
		return Collections.unmodifiableCollection(KNOWN_COLORS);
	}
}
