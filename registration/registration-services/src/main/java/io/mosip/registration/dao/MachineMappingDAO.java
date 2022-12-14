package io.mosip.registration.dao;

import java.util.List;

import io.mosip.registration.entity.MachineMaster;
import io.mosip.registration.entity.UserMachineMapping;
import io.mosip.registration.exception.RegBaseCheckedException;

/**
 * This class is used to update the mapping of users and devices to the
 * Registration Center Machine
 * 
 * @author YASWANTH S
 * @author Brahmananda Reddy
 * @since 1.0.0
 *
 */
public interface MachineMappingDAO {

	/**
	 * This method is used to get station id by using machine name
	 * 
	 * @param machineName
	 * @return
	 * @throws RegBaseCheckedException
	 */
	String getStationID(String machineName) throws RegBaseCheckedException;

	/**
	 * This method is used to get the user mapping details that are mapped to the
	 * given machine id.
	 * 
	 * @param machineId machine ID
	 * @return It returns the list of users against the machine
	 */
	List<UserMachineMapping> getUserMappingDetails(String machineId);


	/**
	 * This method is used to find whether the user exists or not
	 * 
	 * @param userId userId
	 * @return is exists or not
	 */
	boolean isExists(String userId);

	/**
	 * This method is used to get the key index of the Machine based on MachineName
	 * 
	 * @param machineName aka host name
	 * @return
	 */
	String getKeyIndexByMachineName(String machineName);


	MachineMaster getMachine();

}
