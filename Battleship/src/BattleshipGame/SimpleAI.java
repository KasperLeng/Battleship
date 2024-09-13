package BattleshipGame;

import java.awt.Color;

import javax.swing.*;

public class SimpleAI {

	public void makeMove(int playerGrid[][], JButton playerButtons[][], int shipLengths[], int numShipHitsP[],
			int playerShipPos[][]) {
		// generate random coordinates
		int x = (int) (Math.random() * 10);
		int y = (int) (Math.random() * 10);
		// regenerate new coordinates if the current one is invalid
		while (playerGrid[x][y] == -5 || playerGrid[x][y] > 5) {
			x = (int) (Math.random() * 10);
			y = (int) (Math.random() * 10);
		}
		// if miss
		if (playerGrid[x][y] <= 0) {
			playerGrid[x][y] = -5;
			playerButtons[x][y].setBackground(new Color(57, 255, 20));
		}
		// if hit
		else {
			int shipIndex = playerGrid[x][y] - 1;
			playerGrid[x][y] += 5;// mark as hit
			numShipHitsP[shipIndex]++;// count the number of hits
			playerButtons[x][y].setBackground(new Color(202, 39, 39));// display a hit ship

			int shipNum = shipSunk(shipIndex, shipLengths, numShipHitsP);

			if (shipNum > 0) {
				// determine name of ship that has been sunk
				String shipName = "";
				if (shipNum == 1) {
					shipName = "Carrier";
				} else if (shipNum == 2) {
					shipName = "Battleship";
				} else if (shipNum == 3) {
					shipName = "Cruiser";
				} else if (shipNum == 4) {
					shipName = "Submarine";
				} else {
					shipName = "Destroyer";
				}
				// tell user message
				JOptionPane.showMessageDialog(null, "The computer has sunk your " + shipName + "!", "Ship Sunk!",
						JOptionPane.INFORMATION_MESSAGE, null);// rmb to change icon of sunken ship
				// determine values for surrounding border of ship --> NEW
				int rowEnd;
				int colEnd;
				int rowStart;
				int colStart;
				if (playerShipPos[shipIndex][2] == 0) {
					rowEnd = Math.min(playerGrid.length - 1, playerShipPos[shipIndex][0] + shipLengths[shipIndex]);
					colEnd = Math.min(playerGrid[0].length - 1, playerShipPos[shipIndex][1] + 1);
					rowStart = Math.max(0, playerShipPos[shipIndex][0] - 1);
					colStart = Math.max(0, playerShipPos[shipIndex][1] - 1);
				} else {
					rowEnd = Math.min(playerGrid.length - 1, playerShipPos[shipIndex][0] + 1);
					colEnd = Math.min(playerGrid[0].length - 1, playerShipPos[shipIndex][1] + shipLengths[shipIndex]);
					rowStart = Math.max(0, playerShipPos[shipIndex][0] - 1);
					colStart = Math.max(0, playerShipPos[shipIndex][1] - 1);
				}
				// mark the borders of the ship as miss
				for (int r = rowStart; r <= rowEnd; r++) {
					for (int c = colStart; c <= colEnd; c++) {
						if (playerGrid[r][c] < 0) {
							System.out.println(playerGrid[r][c]);
							playerGrid[r][c] = -5;
							// Surrounding color
							playerButtons[r][c].setBackground(new Color(57, 255, 20));
						}
					}
				}

				// check if all ships are sunk
				if (isAllShipsSunk(numShipHitsP)) {
					JOptionPane.showMessageDialog(null,
							"Unfortunately, you lose... :(\nThe computer has sunk all your ships before you.",
							"Ship Sunk!", JOptionPane.INFORMATION_MESSAGE, null);// rmb to change icon of sunken ship
				}
			}
		}
	}

	/**
	 * This method will check if a ship is sunk
	 * 
	 * @param shipIndex
	 * @param shipLengths
	 * @param numShipHitsP
	 * @return is sunk, return the number of the ship, otherwise return -1
	 */
	public int shipSunk(int shipIndex, int shipLengths[], int numShipHitsP[]) {
		if (numShipHitsP[shipIndex] == shipLengths[shipIndex]) {
			numShipHitsP[shipIndex] = -1; // ship has already been sunk
			return shipIndex + 1;
		}
		return -1;
	}

	/**
	 * This method will check if all the players ships have been sunk
	 * 
	 * @param numShipHitsP
	 * @return
	 */
	public boolean isAllShipsSunk(int numShipHitsP[]) {
		for (int i = 0; i < numShipHitsP.length; i++) {
			if (numShipHitsP[i] != -1) {
				return false;
			}
		}
		return true;
	}
}
