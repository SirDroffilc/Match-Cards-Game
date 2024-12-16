public class User {
    private String name;
    private String password;
    private int bestScore;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.bestScore = 100;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getBestScore() {
        return bestScore;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBestScore(int bestScore) {
        if (bestScore < this.bestScore) {
            this.bestScore = bestScore;
        }
    }

    public void displayUserDetails() {
        System.out.println("Name: " + name);
        System.out.println("Best Score: " + bestScore);
    }

    public void setUser(User newUser) {
        this.name = newUser.getName();
        this.password = newUser.getPassword();
    }

}
