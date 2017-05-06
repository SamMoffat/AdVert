package edu.sammoffat.advert.gsons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdvertList {

	@SerializedName("paint_interior")
	@Expose
	private boolean paintInterior;
	@SerializedName("paint_exterior")
	@Expose
	private boolean paintExterior;
	@SerializedName("garden_grass")
	@Expose
	private boolean gardenGrass;
	@SerializedName("garden_flowers")
	@Expose
	private boolean gardenFlowers;
	@SerializedName("garden_trees")
	@Expose
	private boolean gardenTrees;
	@SerializedName("guitar")
	@Expose
	private boolean guitar;
	@SerializedName("piano")
	@Expose
	private boolean piano;
	@SerializedName("drums")
	@Expose
	private boolean drums;
	@SerializedName("sax")
	@Expose
	private boolean sax;
	@SerializedName("trumpet")
	@Expose
	private boolean trumpet;
	@SerializedName("flute")
	@Expose
	private boolean flute;
	@SerializedName("diy")
	@Expose
	private boolean diy;

	public boolean isPaintInterior() {
		return paintInterior;
	}

	public void setPaintInterior(boolean paintInterior) {
		this.paintInterior = paintInterior;
	}

	public boolean isPaintExterior() {
		return paintExterior;
	}

	public void setPaintExterior(boolean paintExterior) {
		this.paintExterior = paintExterior;
	}

	public boolean isGardenGrass() {
		return gardenGrass;
	}

	public void setGardenGrass(boolean gardenGrass) {
		this.gardenGrass = gardenGrass;
	}

	public boolean isGardenFlowers() {
		return gardenFlowers;
	}

	public void setGardenFlowers(boolean gardenFlowers) {
		this.gardenFlowers = gardenFlowers;
	}

	public boolean isGardenTrees() {
		return gardenTrees;
	}

	public void setGardenTrees(boolean gardenTrees) {
		this.gardenTrees = gardenTrees;
	}

	public boolean isGuitar() {
		return guitar;
	}

	public void setGuitar(boolean guitar) {
		this.guitar = guitar;
	}

	public boolean isPiano() {
		return piano;
	}

	public void setPiano(boolean piano) {
		this.piano = piano;
	}

	public boolean isDrums() {
		return drums;
	}

	public void setDrums(boolean drums) {
		this.drums = drums;
	}

	public boolean isSax() {
		return sax;
	}

	public void setSax(boolean sax) {
		this.sax = sax;
	}

	public boolean isTrumpet() {
		return trumpet;
	}

	public void setTrumpet(boolean trumpet) {
		this.trumpet = trumpet;
	}

	public boolean isFlute() {
		return flute;
	}

	public void setFlute(boolean flute) {
		this.flute = flute;
	}

	public boolean isDiy() {
		return diy;
	}

	public void setDiy(boolean diy) {
		this.diy = diy;
	}

}