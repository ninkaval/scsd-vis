package com;

public interface DashboardListener {

	void categorySelected(int _catID); // rotary knob rotated 

	void sentimentSubmitted(int _prefID, String _cardID); // rfid card swiped prefID - which rfid has been swiped

	void specialCategorySelected(int _val); // heart btn
}
