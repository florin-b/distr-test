package com.distributie.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.widget.Toast;

import com.distributie.beans.EvenimentNou;
import com.distributie.enums.TipEveniment;

public class LocalStorage {

	private static String objectsPath = "/EventsObjects";

	private Context context;

	public LocalStorage(Context context) {
		this.context = context;
	}

	public void serObject(EvenimentNou event) throws IOException {

		try {

			String file = getObjDir() + File.separator + Math.abs(new Random().nextLong()) + ".obj";

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			JSONOperations jsonEvLivrare = new JSONOperations(context);

			event.setTipEveniment(TipEveniment.ARHIVAT);

			writer.write(jsonEvLivrare.encodeNewEventData(event));
			writer.flush();
			writer.close();

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
		}

	}

	private String getObjDir() {
		String appPath = context.getApplicationContext().getFilesDir().getAbsolutePath() + objectsPath;

		File dir = new File(appPath);

		if (!dir.exists())
			dir.mkdirs();

		return appPath;

	}

	public List<EvenimentNou> getSavedEvents() throws StreamCorruptedException, IOException, ClassNotFoundException {

		List<EvenimentNou> objList = new ArrayList<EvenimentNou>();

		File files = new File(getObjDir());

		for (File file : files.listFiles()) {
			objList.add(getEveniment(file));
		}

		return objList;

	}

	private EvenimentNou getEveniment(File file) throws StreamCorruptedException, IOException, ClassNotFoundException {
		EvenimentNou ev = null;

		BufferedReader reader = new BufferedReader(new FileReader(file));

		String object = "";
		while (reader.read() != -1)
			object += reader.readLine();

		reader.close();
		JSONOperations jsonEvLivrare = new JSONOperations(context);

		ev = jsonEvLivrare.decodeEventData(object);

		return ev;
	}

	public void deleteAllObjects() {

		for (File f : new File(getObjDir()).listFiles())
			f.delete();

	}

	public String getSerializedEvents() {
		JSONOperations jsonData = new JSONOperations(context);
		String serializedData = "";
		try {
			serializedData = jsonData.serializeListEvents(getSavedEvents());
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return serializedData;

	}

}
