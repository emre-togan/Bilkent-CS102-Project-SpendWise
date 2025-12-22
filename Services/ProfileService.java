package Services;

public class ProfileService {
    private final java.util.Map<Long, Object> profiles = new java.util.concurrent.ConcurrentHashMap<>();

    public Object getProfileByUserId(Long userId) {
        return profiles.get(userId);
    }

    public Object updateProfile(Long userId, Object profile) {
        profiles.put(userId, profile);
        return profile;
    }
}
