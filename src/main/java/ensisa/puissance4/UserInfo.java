package ensisa.puissance4;

public class UserInfo {
    private String username;
    private String selectedToken;
    private String selectedGameMode;
    private String secondusername;

    public UserInfo(String username, String selectedToken, String selectedGameMode, String secondusername) {
        this.username = username;
        this.selectedToken = selectedToken;
        this.selectedGameMode = selectedGameMode;
        this.secondusername = secondusername;
    }

    // Ajoutez les getters selon vos besoins
    public String getUsername() {
        return username;
    }

    public String getSelectedToken() {
        return selectedToken;
    }

    public String getSelectedGameMode() {
        return selectedGameMode;
    }
    public String getSecondusername() {
        return secondusername;
    }
}

