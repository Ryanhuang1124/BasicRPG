public class SuperHero extends Hero {

    private boolean isFlying = false;

    public SuperHero(String name, int maxHp, int attackPower) {
        super(name, maxHp, attackPower);
    }

    
    @Override
    public void talk(String msg) {
        System.out.println("【スーパー勇者 " + getName() + "】：「" + msg + "」");
    }

    
    @Override
    public int attack() {
        int baseDamage = super.getAttackPower() * 2;
        int damage = rand.nextInt(baseDamage) + 1;

        if (isFlying) {
            damage = (int)(damage * 0.5);
            System.out.println("スーパー勇者 " + getName() + "行く");
        }

        System.out.println("スーパー勇者 " + getName() + " damage " + damage + " 点");
        return damage;
    }

   
    @Override
    public void takeDamage(int damage) {
        if (isFlying) {
            System.out.println("スーパー勇者 " + getName() + " は飛んでいるので攻撃を回避した！");
            return;
        }
        super.takeDamage(damage);
    }

   
    public void sleep() {
        setHp(getMaxHp());
        System.out.println("スーパー勇者 " + getName() + " 寝る → HP 回復した");
    }

   
    public boolean runAway() {
        System.out.println("スーパー勇者 " + getName() + " やめて逃げる！");
        return true;
    }

   
    public void fly() {
        if (isFlying) {
            System.out.println("飛行中");
            return;
        }
        isFlying = true;
        System.out.println("スーパー勇者 " + getName() + " 飛行無敵時間");
    }

 
    public void land() {
        if (!isFlying) {
            System.out.println("飛行してない");
            return;
        }
        isFlying = false;
        System.out.println("スーパー勇者 " + getName() + "");
    }

    public boolean isFlying() {
        return isFlying;
    }
}