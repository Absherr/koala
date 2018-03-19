/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author ADAM
 */
class StatusList
{
    private String mId;
    private String mStatus;
    StatusList(String pmId, String pmStatus)
    {
        mId = pmId;
        mStatus = pmStatus;
    }
    public String getStatus()
    {
        return mStatus;
    }
    public String getId()
    {
        return mId;
    }
}
