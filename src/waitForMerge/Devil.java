import java.util.Random;

public class devil extends Enemy {
    private String name;
    private int hp;                
    private int maxAttackPower;     
    private Random rand = new Random();

    public devil(String name, int heroAttackPower) {
        this.name = name;
        this.hp = 300;  
        this.maxAttackPower = heroAttackPower * 5;  
    }

   
    public void talk(String message) {
        System.out.println("【魔王 " + name + "】：「" + message + "」");
    }

    
    public int attack() {
        int damage = rand.nextInt(maxAttackPower) + 1;
        System.out.println("魔王 " + name + " 使用『暗黒魔法』 " + damage + " 点");
        return damage;
    }

  
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
        System.out.println("魔王 " + name + " damage " + damage + " （HP：" + hp + "）");
    }

    
    public boolean isDead() {
        return hp <= 0;
    }

   
    public boolean runAway() {
       
        boolean escaped = rand.nextInt(100) < 30;

        if (escaped) {
            System.out.println("魔王 " + name + "『まさーか』 ");
        } else {
            System.out.println("魔王 " + name + " 逃げる失敗");
        }
        return escaped;
    }

    public int getHp() {
        return hp;
    }
}
