package com.javarush.games.minigames.mini05;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

/* 
Цвета
радуги
*/

public class RainbowGame extends Game {

	//напишите тут ваш код

	@Override
	public void initialize() {
		setScreenSize(10, 7);
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 10; j++) {
				if(i == 0) {
					setCellColor(j, i, Color.RED);
				} else if(i == 1) {
					setCellColor(j, i, Color.ORANGE);
				} else if(i == 2) {
					setCellColor(j, i, Color.YELLOW);
				} else if(i == 3) {
					setCellColor(j, i, Color.GREEN);
				} else if(i == 4) {
					setCellColor(j, i, Color.BLUE);
				} else if(i == 5) {
					setCellColor(j, i, Color.INDIGO);
				} else {
					setCellColor(j, i, Color.VIOLET);
				}
			}
		}

	}
}
