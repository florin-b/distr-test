package com.distributie.listeners;

import com.distributie.enums.EnumOpConfirm;

public interface AlertDialogListener {
	void alertDialogOk(EnumOpConfirm tipOperatie);

	void alertDialogCancel(EnumOpConfirm tipOperatie);

}
