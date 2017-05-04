package com.distributie.model;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.content.Context;
import android.content.Intent;

import android.os.Process;

public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
	private final Context myContext;

	public ExceptionHandler(Context context) {
		myContext = context;
	}

	public void uncaughtException(Thread thread, Throwable exception) {
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));

		System.err.println(stackTrace);

		Intent intent = new Intent(myContext, CrashReport.class);
		intent.putExtra(CrashReport.STACKTRACE, stackTrace.toString() + " User:" + UserInfo.getInstance().getNume());
		myContext.startActivity(intent);

		Process.killProcess(Process.myPid());
		System.exit(10);
	}
}
