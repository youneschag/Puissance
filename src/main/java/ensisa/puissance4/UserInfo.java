package ensisa.puissance4;

public class UserInfo {
    private String username;
    private String selectedToken;
    private String selectedGameMode;
    private String secondusername;
    private String selectedOrdre;
    private int selectedTimeLimit;

    public UserInfo(String username, String selectedToken, String selectedGameMode, String secondusername, String selectedOrdre, int selectedTimeLimit) {
        this.username = username;
        this.selectedToken = selectedToken;
        this.selectedGameMode = selectedGameMode;
        this.secondusername = secondusername;
        this.selectedOrdre = selectedOrdre;
        this.selectedTimeLimit = selectedTimeLimit;
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

    public String getSelectedOrdre() {
        return selectedOrdre;
    }

    public int getSelectedTimeLimit() {
        return selectedTimeLimit;
    }
}

