package main.repository;

public final class RepoFactory {
    private RepoFactory() {
    }

    public static UserRepository getUserRepository(RepoMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("RepoMode cannot be null");
        }
        switch (mode) {
            case MEMORY:
                return MemoryUserRepository.getInstance();
            case FILE:
                return FileUserRepository.getInstance();
            case DB:
                return DBUserRepository.getInstance();
            default:
                throw new IllegalArgumentException("Unsupported RepoMode: " + mode);
        }
    }

    public static SubjectRepository getSubjectRepository(RepoMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("RepoMode cannot be null");
        }
        switch (mode) {
            case MEMORY:
                return MemorySubjectRepository.getInstance();
            case FILE:
                return FileSubjectRepository.getInstance();
            case DB:
                return DBSubjectRepository.getInstance();
            default:
                throw new IllegalArgumentException("Unsupported RepoMode: " + mode);
        }
    }
}

