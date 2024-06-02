package dev.mrturtle.analog.config;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AnalogConfig {
	public String _c0 = "Config version, do not modify this!";
	@SerializedName("config_version")
	public int configVersion = ConfigManager.CONFIG_VERSION;
	public String _c1 = "The number of usable radio channels, values over 100 will cause visual issues with GUIs but are otherwise functional";
	@SerializedName("max_radio_channels")
	public int maxRadioChannels = 100;
	public String _c2 = "The max radius radios can pickup voices from";
	@SerializedName("radio_listening_distance")
	public int radioListeningDistance = 8;
}
