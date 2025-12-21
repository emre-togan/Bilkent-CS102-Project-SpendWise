package Services;

import java.util.HashMap;
import java.util.Map;

// DB'de settings tablosu repo içinde kesin değil.
// Bu yüzden şimdilik in-memory tutuluyor. Sonra tablo eklenince DB'ye taşınır.
public class SettingsService {

    // userId -> (settingName -> value)
    private static final Map<Integer, Map<String, Boolean>> store = new HashMap<>();

    public boolean updateSettings(int userId, Map<String, Boolean> settings) {
        if (settings == null) return false;
        store.put(userId, new HashMap<>(settings));
        return true;
    }

    public Map<String, Boolean> getSettings(int userId) {
        Map<String, Boolean> s = store.get(userId);
        return (s == null) ? new HashMap<>() : new HashMap<>(s);
    }

    public boolean toggleNotification(int userId, String type, boolean enabled) {
        if (type == null || type.trim().isEmpty()) return false;
        Map<String, Boolean> s = store.getOrDefault(userId, new HashMap<>());
        s.put(type.trim(), enabled);
        store.put(userId, s);
        return true;
    }
}
