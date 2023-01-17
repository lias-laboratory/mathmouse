package fr.ensma.lias.dockermanagerapi;

import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

/**
 * Interfaces containing necessary operations to handle scaling operations in
 * docker using docker-compose.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public interface DockerCommandsManager {
    /**
     * Adds an instance of the given service.
     * 
     * @param serviceName
     */
    void incrementeInstanceService( EServiceName serviceName );

    /**
     * Removes one instance of the given service.
     * 
     * @param serviceName
     */
    void decrementeInstanceService( EServiceName serviceName );

    /**
     * Creates as many instances of the given service as ordered by
     * numberOFInstances (if the service already has some instances, they will
     * rather be completed or deleted to reach the new required number).
     * 
     * @param serviceName
     * @param numberOfInstances
     */
    void scale( EServiceName serviceName, int numberOfInstances );

    /**
     * Returns the number of instances of the given service.
     * 
     * @param serviceName
     * @return
     */
    void instancesOfService( EServiceName serviceName );

}
