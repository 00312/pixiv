package sckdn.lisa.model;

import java.io.Serializable;

import sckdn.lisa.interfaces.UserContainer;

public class UserDetailResponse extends UserHolder implements Serializable, UserContainer {

    private ProfileBean profile;
    private ProfilePublicityBean profile_publicity;
    private WorkspaceBean workspace;

    public ProfileBean getProfile() {
        return profile;
    }

    public void setProfile(ProfileBean profile) {
        this.profile = profile;
    }

    public ProfilePublicityBean getProfile_publicity() {
        return profile_publicity;
    }

    public void setProfile_publicity(ProfilePublicityBean profile_publicity) {
        this.profile_publicity = profile_publicity;
    }

    public WorkspaceBean getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceBean workspace) {
        this.workspace = workspace;
    }

    @Override
    public int getUserId() {
        return getUser() == null ? 0 : getUser().getId();
    }
}
