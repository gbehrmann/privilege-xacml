/*
 * LocalId.java
 *
 * Created on January 5, 2005, 5:32 PM
 */

package org.opensciencegrid.authz.xacml.common;

/** Represents a local user identity, as estabilished by the GRID Identity
 * Mapping Service.
 *
 * @author Gabriele Carcassi and Markus Lorch
 */
public class LocalId {

    private String userName;
    private String groupName;
    private String[] supplementalGroupNames;
    // the following are storage specific and should really be in another class
    private String fsRootPath;
    private String rootPath;
    private String relHomePath;
    private String uid;
    private String gid;
    private String[] secondary_gids;
    private String priority;
    private boolean readOnlyFlag = false;

    public LocalId() {
    }

    /* this constructor does not initialize all fields */
    public LocalId(String userName, String groupName, String[] supplementalGroupNames,
                              String rootPath, String relHomePath) {

      this.userName = userName;
      this.groupName = groupName;
      if(supplementalGroupNames != null) {
        this.supplementalGroupNames = new String[supplementalGroupNames.length];
        for(int i=0; i < supplementalGroupNames.length; i++)
           this.supplementalGroupNames[i] = supplementalGroupNames[i];
      }
      this.rootPath = rootPath;
      this.relHomePath = relHomePath;

    }




    /**
     * The username for the local identity.
     * @return a UNIX username (i.e. "carcassi").
     */
    public String getUserName() {

        return this.userName;
    }

    /**
     * Changes the  username for the local identity.
     * @param userName a UNIX username (i.e. "carcassi").
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }

    /**
     * The user ID for the local identity.
     * (to be used in place of username, shall take precedence if set)
     * @return a UNIX uid as a string
     */
    public String getUID() {

        return this.uid;
    }

    /**
     * Changes the  UID for the local identity.
     * (to be used in place of username, shall take precedence if set)
     * @param uid a UNIX user id as a string
     */
    public void setUID(String uid) {

        this.uid = uid;
    }

    /**
     * The primary group for the local identity.
     * @return a UNIX group name (i.e. "atlas").
     */
     public String getGroupName() {
        return this.groupName;
    }


    /**
     * Changes the primary group for the local identity.
     * @param g a UNIX group name (i.e. "atlas").
     */

     public void setGroupName(String g) {
       this.groupName = g;
     }

    /**
     * The group ID for the local identity.
     * (to be used in place of groupname, shall take precedence if set)
     * @return a UNIX gid as a string
     */
    public String getGID() {

        return this.gid;
    }

    /**
     * Changes the  GID for the local identity.
     * (to be used in place of groupname, shall take precedence if set)
     * @param gid a UNIX group id as a string
     */
    public void setGID(String gid) {

        this.gid = gid;
    }

    /**
     * The secondary group IDs for the local identity.
     * (to be used in place of groupname, shall take precedence if set)
     * @return an array of UNIX gids as strings
     */
    public String[] getSecondaryGIDs() {

        return this.secondary_gids;
    }

    /**
     * Changes the  GID for the local identity.
     * (to be used in place of groupname, shall take precedence if set)
     * @param gids an array of UNIX group ids as strings
     */
    public void setSecondaryGIDs(String[] gids) {

        this.secondary_gids = gids;
    }

    /**

    /**
     * Getter for property supplementalGroups.
     * @return Value of property supplementalGroups.
     */
    public String[] getSupplementalGroupNames() {

        return this.supplementalGroupNames;
    }

    /**
     * Setter for property supplementalGroups.
     * @param sg New value of property supplementalGroups.
     */
    public void setSupplementalGroupNames(String[] sg) {
        this.supplementalGroupNames = new String[sg.length];
        for(int i=0; i < sg.length; i++)
           this.supplementalGroupNames[i] = sg[i];
    }


    /**
     * Getter for property rootPath.
     * @return Value of property rootPath.
     */
    public String getRootPath() {

        return this.rootPath;
    }

    /**
     * Setter for property rootPath.
     * @param rootPath New value of property rootPath.
     */
    public void setRootPath(String rootPath) {

        this.rootPath = rootPath;
    }

    /**
     * Getter for property relHomePath.
     * @return Value of property relHomePath.
     */
    public String getRelativeHomePath() {

        return this.relHomePath;
    }

    /**
     * Setter for property relHomePath.
     * @param relHomePath New value of property relHomePath.
     */
    public void setRelativeHomePath(String relHomePath) {

        this.relHomePath = relHomePath;
    }

    /**
     * Getter for property fsRootPath.
     * @return Value of property fsRootPath.
     */
    public String getFSRootPath() {

        return this.fsRootPath;
    }

    /**
     * Setter for property fsRootPath.
     * @param fsRootPath New value of property fsRootPath.
     */
    public void setFSRootPath(String fsRootPath) {

        this.fsRootPath = fsRootPath;
    }

    /**
     * Getter for property priority.
     * @return Value of property priority.
     */
    public String getPriority() {

        return this.priority;
    }

    /**
     * Setter for property priority.
     * @param priority New value of property priority.
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Getter for readOnlyFlag.
     * @return true/false depending on readOnlyFlag.
     */
    public boolean getReadOnlyFlag() {

        return this.readOnlyFlag;
    }

    /**
     * Setter for property readOnlyFlag.
     * @param readOnlyFlag New value of property readOnlyFlag
     */
    public void setReadOnlyFlag(boolean readOnlyFlag) {
        this.readOnlyFlag = readOnlyFlag;
    }


    /** simple toString method for debug output */
    public String toString() {
        StringBuffer sb = new StringBuffer("LocalId[");
        if (userName != null) {
            sb.append("userName: ");
            sb.append(userName);
        }
        if (groupName != null) {
            if (sb.length() != 8) sb.append(" - ");
            sb.append("groupName: ");
            sb.append(groupName);
        }
        if (rootPath != null) {
            if (sb.length() != 8) sb.append(" - ");
            sb.append("rootPath: ");
            sb.append(rootPath);
        }
        if (relHomePath != null) {
            if (sb.length() != 8) sb.append(" - ");
            sb.append("relHomePath: ");
            sb.append(relHomePath);
        }
        if (fsRootPath != null) {
            if (sb.length() != 8) sb.append(" - ");
            sb.append("fsRootPath: ");
            sb.append(fsRootPath);
        }
        if (uid != null) {
            if (sb.length() != 8) sb.append(" - ");
            sb.append("UID: ");
            sb.append(uid.toString());
        }
        if (gid != null) {
            if (sb.length() != 8) sb.append(" - ");
            sb.append("GID: ");
            sb.append(gid.toString());
        }
        if (priority != null) {
            if (sb.length() != 8) sb.append(" - ");
            sb.append("Priority: ");
            sb.append(priority.toString());
        }
        if (readOnlyFlag == true) {
            if (sb.length() != 8) sb.append(" - ");
            sb.append("ReadOnlyFlag = true");
        }
        sb.append(']');
        return sb.toString();
    }


}