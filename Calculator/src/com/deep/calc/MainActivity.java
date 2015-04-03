package com.deep.calc;

import java.io.Console;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	int Answer = 0, inputNumber = 0, inputNumber2 = 0, executed = 0, Flag = 0;
	int numberOne = 0, numberTwo = 0, numberThree = 0;

	Boolean isAdd = false, isMinus = false, pressNumber = true;
	Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
			btnClear, btnAdd, btnSub, btnEquals;
	EditText AnswerDisplay;
	TextView Mesg;
	String TempNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calc);
		btn0 = (Button) findViewById(R.id.btn0);
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn4 = (Button) findViewById(R.id.btn4);
		btn5 = (Button) findViewById(R.id.btn5);
		btn6 = (Button) findViewById(R.id.btn6);
		btn7 = (Button) findViewById(R.id.btn7);
		btn8 = (Button) findViewById(R.id.btn8);
		btn9 = (Button) findViewById(R.id.btn9);
		btnClear = (Button) findViewById(R.id.btnClear);
		btnAdd = (Button) findViewById(R.id.btnPlus);
		btnSub = (Button) findViewById(R.id.btnMinus);
		btnEquals = (Button) findViewById(R.id.btnEquals);
		AnswerDisplay = (EditText) findViewById(R.id.edtAnswer);
		Mesg = (TextView) findViewById(R.id.Message);

		// for 0
		btn0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 1) {
					AnswerDisplay.setText(null);
				}
				AnswerDisplay.setText(AnswerDisplay.getText() + "0");
				executed = 0;
			}
		});
		// for 1
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (executed == 1) {
					AnswerDisplay.setText(null);
				}
				// Checks if the given number starts with 0, if so replaces it
				// with 1 . (Same applies to all the numbers)
				if (AnswerDisplay.getText().toString().startsWith("0")) {
					AnswerDisplay.setText("1");
				} else
					AnswerDisplay.setText(AnswerDisplay.getText() + "1");
				executed = 0;
			}
		});
		// for 2
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 1) {
					AnswerDisplay.setText(null);
				}
				if (AnswerDisplay.getText().toString().startsWith("0")) {
					AnswerDisplay.setText("2");
				} else
					AnswerDisplay.setText(AnswerDisplay.getText() + "2");
				executed = 0;
			}
		});
		// for 3
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 1) {
					AnswerDisplay.setText(null);
				}
				if (AnswerDisplay.getText().toString().startsWith("0")) {
					AnswerDisplay.setText("3");
				} else
					AnswerDisplay.setText(AnswerDisplay.getText() + "3");
				executed = 0;
			}
		});
		// for 4
		btn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 1) {
					AnswerDisplay.setText(null);
				}
				if (AnswerDisplay.getText().toString().startsWith("0")) {
					AnswerDisplay.setText("4");
				} else
					AnswerDisplay.setText(AnswerDisplay.getText() + "4");
				executed = 0;
			}
		});
		// for 5
		btn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 1) {
					AnswerDisplay.setText(null);
				}
				if (AnswerDisplay.getText().toString().startsWith("0")) {
					AnswerDisplay.setText("5");
				} else
					AnswerDisplay.setText(AnswerDisplay.getText() + "5");
				executed = 0;
			}
		});
		// for 6
		btn6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 1) {
					AnswerDisplay.setText(null);
				}
				if (AnswerDisplay.getText().toString().startsWith("0")) {
					AnswerDisplay.setText("6");
				} else
					AnswerDisplay.setText(AnswerDisplay.getText() + "6");
				executed = 0;
			}
		});
		// for 7
		btn7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 1) {
					AnswerDisplay.setText(null);
				}
				if (AnswerDisplay.getText().toString().startsWith("0")) {
					AnswerDisplay.setText("7");
				} else
					AnswerDisplay.setText(AnswerDisplay.getText() + "7");
				executed = 0;
			}
		});
		// for 8
		btn8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 1) {
					AnswerDisplay.setText(null);
				}
				if (AnswerDisplay.getText().toString().startsWith("0")) {
					AnswerDisplay.setText("8");
				} else
					AnswerDisplay.setText(AnswerDisplay.getText() + "8");
				executed = 0;
			}
		});
		// for 9
		btn9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 1) {
					AnswerDisplay.setText(null);
				}
				if (AnswerDisplay.getText().toString().startsWith("0")) {
					AnswerDisplay.setText("9");
				} else
					AnswerDisplay.setText(AnswerDisplay.getText() + "9");
				executed = 0;
			}
		});
		// for Clear
		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AnswerDisplay.setText(null);
				numberOne = 0;
				numberThree = 0;
				Answer = 0;
				numberTwo = 0;
				Flag = 0;
				executed = 0;

			}
		});
		// for Add
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 0) {
					// This is to check if the user entered +/- sign before
					// entering any number
					// if any consecutive operators are entered, in that case
					// last entered operand will be stored in flag
					if (!AnswerDisplay.getText().toString().matches("^[0-9]+$")) {
						Flag = 0;
						return;
					} else
						numberOne = Integer.parseInt(AnswerDisplay.getText().toString());
					// Below if checks for max boundary. max length should be 7
					if ((Flag == 1 && Integer.toString(Answer - numberOne).length() > 7)
							|| (Flag == 0 && Answer + numberOne > 9999999)) {
						Answer = 0;
						Flag = 0;
					} else {
						if (Flag == 1) // if flag==1, previous operator entered
										// was a minus sign
							Answer = Answer - numberOne;
						else
							Answer = Answer + numberOne;
						Flag = 0;
					}
					executed = 1;
					AnswerDisplay.setText(Answer + "");
				} else
					Flag = 0;
			}
		});
		// for Subtract
		btnSub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 0) {
					// This is to check if the user entered +/- sign before
					// entering any number
					if (!AnswerDisplay.getText().toString().matches("^[0-9]+$")) {
						Flag = 1;
						return;
					}

					else
						numberTwo = Integer.parseInt(AnswerDisplay.getText().toString());
					// Below if checks for max boundary. max length should be 7
					if ((Flag == 1 && Integer.toString(Answer - numberTwo).length() > 7)
							|| (Flag == 0 && Answer + numberTwo > 9999999)) {
						Answer = 0;
						Flag = 0;
					} else {
						if (Flag == 1) // if flag==1, previous operator entered
										// was a minus sign
							Answer = Answer - numberTwo;
						else
							Answer = Answer + numberTwo;
						Flag = 1;
					}
					executed = 1;
					AnswerDisplay.setText(Answer + "");

				} else
					Flag = 1;
			}
		});
		// for equals
		btnEquals.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (executed == 0) {
					numberThree = Integer.parseInt(AnswerDisplay.getText().toString());
					if (Flag == 1) {
						// // Below if checks for max boundary. max length
						// should be 7
						if (Integer.toString(Answer - numberThree).length() > 7)
							Answer = 0;
						else
							Answer = Answer - numberThree;
					} else if (Answer + numberThree > 9999999)
						Answer = 0;
					else
						Answer = Answer + numberThree;

					AnswerDisplay.setText(Answer + "");
					if (Answer < 0) {
						Flag = 0;
					} else {
						Flag = 0;
					}
					executed = 1;
				} else {
					return;

				}
			}
		});

	}

	public void ShowToast() {
		Toast.makeText(getApplicationContext(), "Max 7 Digit Number only",
				Toast.LENGTH_SHORT);
	}

	public void ShowAnswer() {
		AnswerDisplay.setText(Answer);
	}

	public void Calc() {
		if (isAdd) {
			inputNumber2 = Integer.parseInt(AnswerDisplay.getText().toString());
			Answer += inputNumber2;
			isAdd = false;
			AnswerDisplay.setText("" + Answer);
		} else if (isMinus) {
			inputNumber2 = Integer.parseInt(AnswerDisplay.getText().toString());
			Answer -= inputNumber2;
			isMinus = false;
			AnswerDisplay.setText("" + Answer);
		} else {
			inputNumber = Integer.parseInt(AnswerDisplay.getText().toString());
			Answer = inputNumber;
			AnswerDisplay.setText("" + Answer);
		}
	}

}
