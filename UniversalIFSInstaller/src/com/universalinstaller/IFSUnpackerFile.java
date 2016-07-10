package com.universalinstaller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class IFSUnpackerFile
{
	public File file;
	public File ifsFile;
	public Boolean isZip;
	public Boolean replace;
	public Boolean remove;

	protected IFSUnpackerFile(){}

	public IFSUnpackerFile(File input, File ifsfile, boolean zip, boolean replace, boolean remove)
	{
		this.file = input;
		this.ifsFile = ifsfile;
		this.isZip = zip;
		this.replace = replace;
		this.remove = remove;
	}

	public static List<IFSUnpackerFile> load() throws IFSUnpackerException
	{
		File configFile = new File("files.json");
		if(!configFile.isFile()){
			try{
				IFSUnpackerFile sample = new IFSUnpackerFile(new File("sample.zip"),null,true,false,false);
				String sampleJson = new GsonBuilder().setPrettyPrinting().create().toJson(Arrays.asList(sample));
				Files.write(configFile.toPath(),sampleJson.getBytes(StandardCharsets.UTF_8));
			} catch (IOException e){}
			throw new IFSUnpackerException(String.format("Config File %s Not Found, A Sample Has Been Created.", configFile.getAbsolutePath()));
		}

		try{
			String json = new String(Files.readAllBytes(configFile.toPath()),"UTF8");
			return new Gson().fromJson(json, new TypeToken<List<IFSUnpackerFile>>(){}.getType());
		} catch (IOException | JsonSyntaxException e){
			throw new IFSUnpackerException(String.format("Error Reading Config File %s", configFile.getAbsolutePath()),e);
		}
	}
}
