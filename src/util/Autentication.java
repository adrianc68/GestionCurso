package util;

import domain.usuarios.Personal;
import domain.database.dao.HostDAO;
import domain.database.dao.UsuarioDAO;
import util.exceptions.LimitReachedException;
import util.exceptions.UserNotFoundException;
import java.sql.SQLException;

public class Autentication {
    private static Autentication instance;
    private Personal currentUser;

    public static Autentication getInstance() {
        if(instance == null) {
            instance = new Autentication();
        }
        return instance;
    }

    public Personal getCurrentUser() {
        return currentUser;
    }

    public void logOut() {
        currentUser = null;
    }

    public boolean logIn(String email, String password) throws UserNotFoundException, LimitReachedException, SQLException {
        boolean isLogged;
        checkAttemptsLimit();
        sendMacAddress();
        Personal user = getCurrentUser(email, password);
        resetAttempts();
        isLogged = true;
        currentUser = user;
        return isLogged;
    }

    private Personal getCurrentUser(String nombreUsuario, String contraseña) throws UserNotFoundException, SQLException {
        Personal person = new UsuarioDAO().obtenerPersonalPorUsuarioYContraseña(nombreUsuario, contraseña);
        if(person == null) {
            throw new UserNotFoundException("¡Revisa tus datos! ¡Puede ser incorrecta el usuario o la contraseña");
        }
        return person;
    }

    private void checkAttemptsLimit() throws LimitReachedException, SQLException {
        final int ATTEMPTS_LIMIT = 5;
        boolean isAttempsLimitReached = new HostDAO().getAttemptsByMacAddress( getMacAddress() ) == ATTEMPTS_LIMIT;
        if(isAttempsLimitReached) {
            throw new LimitReachedException("¡Limite de intentos alcanzado! ¡Espera 10 minutos!");
        }
    }

    private String getMacAddress() {
        String macAddress = NetworkAddress.getLocalAdress();
        String macAddressEncrypted = Cryptography.cryptSHA2( (macAddress != null) ? macAddress : "" );
        return macAddressEncrypted;
    }

    private boolean sendMacAddress() throws SQLException {
        boolean isMacAddressSent;
        isMacAddressSent = new HostDAO().sendActualMacAddress( getMacAddress() );
        return isMacAddressSent;
    }

    private boolean resetAttempts() throws SQLException {
        boolean isAttemptsReset;
        isAttemptsReset = new HostDAO().resetAttempts( getMacAddress() );
        return isAttemptsReset;
    }

    private void Auth() {
    }

}
