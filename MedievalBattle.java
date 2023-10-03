import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.io.File;

abstract class Character {
    private String name;
    private int health;
    private int strength;
    private int defense;
    private int agility;
    private int damageDice;
    private int damageSides;

    public Character(String name, int health, int strength, int defense, int agility, int damageDice, int damageSides) {
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.defense = defense;
        this.agility = agility;
        this.damageDice = damageDice;
        this.damageSides = damageSides;
    }

    public abstract int rollInitiative();

    public abstract int rollAttack();

    public abstract int rollDefense();

    public abstract int rollDamage();

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public int getAgility() {
        return agility;
    }

    public int getStrength() {
        return strength;
    }

    public int getDefense() {
        return defense;
    }

    public int getDamageDice() {
        return damageDice;
    }

    public int getDamageSides() {
        return damageSides;
    }
}

class Hero extends Character {
    public Hero(String name, int health, int strength, int defense, int agility, int damageDice, int damageSides) {
        super(name, health, strength, defense, agility, damageDice, damageSides);
    }

    @Override
    public int rollInitiative() {
        Random rand = new Random();
        return rand.nextInt(10) + getAgility();
    }

    @Override
    public int rollAttack() {
        Random rand = new Random();
        return rand.nextInt(10) + getAgility() + getStrength();
    }

    @Override
    public int rollDefense() {
        Random rand = new Random();
        return rand.nextInt(10) + getAgility() + getDefense();
    }

    @Override
    public int rollDamage() {
        Random rand = new Random();
        int totalDamage = 0;
        for (int i = 0; i < getDamageDice(); i++) {
            totalDamage += rand.nextInt(getDamageSides()) + 1;
        }
        return totalDamage;
    }
}

class Monster extends Character {
    public Monster(String name, int health, int strength, int defense, int agility, int damageDice, int damageSides) {
        super(name, health, strength, defense, agility, damageDice, damageSides);
    }

    @Override
    public int rollInitiative() {
        Random rand = new Random();
        return rand.nextInt(10) + getAgility();
    }

    @Override
    public int rollAttack() {
        Random rand = new Random();
        return rand.nextInt(10) + getAgility() + getStrength();
    }

    @Override
    public int rollDefense() {
        Random rand = new Random();
        return rand.nextInt(10) + getAgility() + getDefense();
    }

    @Override
    public int rollDamage() {
        Random rand = new Random();
        int totalDamage = 0;
        for (int i = 0; i < getDamageDice(); i++) {
            totalDamage += rand.nextInt(getDamageSides()) + 1;
        }
        return totalDamage;
    }
}

public class MedievalBattle {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-vindo ao Medieval Battle!");
        System.out.print("Informe o seu nickname: ");
        String playerNickname = scanner.nextLine();
        Character hero = chooseHero(playerNickname);

        Character[] monsters = {
            new Monster("Morto Vivo", 25, 4, 0, 1, 2, 4),
            new Monster("Orc", 20, 6, 2, 2, 1, 8),
            new Monster("Kobold", 20, 4, 2, 4, 3, 2)
        };

        Character monster = chooseMonster(monsters);

        int rounds = 0;

        while (hero.getHealth() > 0 && monster.getHealth() > 0) {
            rounds++;
            int heroInitiative = hero.rollInitiative();
            int monsterInitiative = monster.rollInitiative();
            if (heroInitiative > monsterInitiative) {
                performBattle(hero, monster, rounds);
            } else if (monsterInitiative > heroInitiative) {
                performBattle(monster, hero, rounds);
            }
        }

        String result = (hero.getHealth() > 0) ? "GANHOU" : "PERDEU";
        saveBattleLog(playerNickname, hero.getName(), result, monster.getName(), rounds);

        if (hero.getHealth() <= 0) {
            printGameOver();
        } else {
            System.out.println("Você derrotou o " + monster.getName() + "!");
        }
    }

    private static Character chooseHero(String playerNickname) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha a classe do herói:");
        System.out.println("1. Guerreiro");
        System.out.println("2. Bárbaro");
        System.out.println("3. Paladino");
        int heroChoice = scanner.nextInt();
        if (heroChoice == 1) {
            return new Hero(playerNickname, 12, 4, 3, 3, 2, 4);
        } else if (heroChoice == 2) {
            return new Hero(playerNickname, 13, 6, 1, 3, 2, 6);
        } else if (heroChoice == 3) {
            return new Hero(playerNickname, 15, 2, 5, 1, 2, 4);
        } else {
            throw new IllegalArgumentException("Escolha de herói inválida.");
        }
    }

    private static Character chooseMonster(Character[] monsters) {
        Random rand = new Random();
        return monsters[rand.nextInt(monsters.length)];
    }

    private static void performBattle(Character attacker, Character defender, int rounds) {
        int attackRoll = attacker.rollAttack();
        int defenseRoll = defender.rollDefense();

        if (attackRoll > defenseRoll) {
            int damage = attacker.rollDamage();
            defender.takeDamage(damage);
            System.out.println("Rodada " + rounds + ": " + attacker.getName() + " ataca " + defender.getName() + " e causa " + damage + " pontos de dano.");
        } else {
            System.out.println("Rodada " + rounds + ": " + attacker.getName() + " ataca " + defender.getName() + " mas nao causa dano.");
        }
    }

    private static void saveBattleLog(String playerNickname, String heroName, String result, String monsterName, int rounds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());

        File tempDir = new File("temp");
        if (!tempDir.exists()) {
            tempDir.mkdirs(); // Cria o diretório "temp" se ele não existir
        }

        try {
            FileWriter writer = new FileWriter("temp/" + playerNickname + ".csv", true);
            writer.write(currentDate + ";" + heroName + ";" + result + ";" + monsterName + ";" + rounds + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void printGameOver() {
        System.out.println("\n   _____          __  __ ______    ______      ________ _____  ");
        System.out.println("  / ____|   /\\   |  \\/  |  ____|  / __ \\ \\    / /  ____|  __ \\ ");
        System.out.println(" | |  __   /  \\  | \\  / | |__    | |  | \\ \\  / /| |__  | |__) |");
        System.out.println(" | | |_ | / /\\ \\ | |\\/| |  __|   | |  | |\\ \\/ / |  __| |  _  / ");
        System.out.println(" | |__| |/ ____ \\| |  | | |____  | |__| | \\  /  | |____| | \\ \\ ");
        System.out.println("  \\_____/_/    \\_\\_|  |_|______|  \\____/   \\/   |______|_|  \\_\\");
    }
}
