org.jsp.model

import java.util.Random;

public class Player {
    private int health;
    private int strength;
    private int attack;
    private Random random;

     public int getStrength() {
			return strength;
		}

		public void setStrength(int strength) {
			this.strength = strength;
		}

		public int getAttack() {
			return attack;
		}

		public void setAttack(int attack) {
			this.attack = attack;
		}

		public Random getRandom() {
			return random;
		}

		public void setRandom(Random random) {
			this.random = random;
		}

		public void setHealth(int health) {
			this.health = health;
		}



    public Player(int health, int strength, int attack) {
        this.health = health;
        this.strength = strength;
        this.attack = attack;
        this.random = new Random();
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public int rollDice() {
        return random.nextInt(6) + 1; // 6-sided die
    }

    public int calculateDamage(int diceRoll) {
        return attack * diceRoll;
    }

    public int calculateDefense(int diceRoll) {
        return strength * diceRoll;
    }
}

org.jsp.dao

public class MagicalArena {
    private Player playerA;
    private Player playerB;

    public MagicalArena(Player playerA, Player playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public void startMatch() {
        Player attacker = playerA.getHealth() < playerB.getHealth() ? playerA : playerB;
        Player defender = attacker == playerA ? playerB : playerA;

        while (playerA.getHealth() > 0 && playerB.getHealth() > 0) {
            int attackRoll = attacker.rollDice();
            int defenseRoll = defender.rollDice();

            int damage = attacker.calculateDamage(attackRoll) - defender.calculateDefense(defenseRoll);
            if (damage > 0) {
                defender.takeDamage(damage);
            }

            // Switch roles for the next turn
            Player temp = attacker;
            attacker = defender;
            defender = temp;
        }

        endMatch();
    }

    private void endMatch() {
        Player winner = playerA.getHealth() > 0 ? playerA : playerB;
        System.out.println("Winner: Player " + (winner == playerA ? "A" : "B"));
    }
}
org.jsp.controller

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MagicalArenaTest {
    @Test
    public void testStartMatch() {
        Player playerA = new Player(50, 5, 10);
        Player playerB = new Player(100, 10, 5);
        MagicalArena arena = new MagicalArena(playerA, playerB);
        arena.startMatch();

        assertTrue(playerA.getHealth() == 0 || playerB.getHealth() == 0);
    }

    @Test
    public void testPlayerTakeDamage() {
        Player player = new Player(100, 10, 5);
        player.takeDamage(30);
        assertEquals(70, player.getHealth());
    }
