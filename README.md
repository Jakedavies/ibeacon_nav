# A research project in indoor navigation using IBeacon technology, with a focus on the blind or partially sighted


## Features

* Android application
  * Indoor location tracking using iBeacon 
    * Proximity based tracking based on single iBeacon proxmity
    * Full 2d tracking based on triangulation of >= 3 iBeacons
  * Sight impaired navigation (Haptic or Audio based)
  * Sight impaired friendly UI navigation 
    * Menu to select a location, (IE. Lakeview Market, Orchard Park Mall)
    * Menu to select a section in said location, (IE. Product section, Food Court)
  
* Server side application to define locations and upload new locations 
  * Hosts locations for android application to download
  * Ability to upload/define new loctations 
  * 

## Requirements

### Non-functional Requirements
* User should be able to navigate app without sight
* User should be able to select between haptic and audio feedback
* User should be able to select their location
* User should be able to select a section in their location to navigate to
* User should be able to get feedback on how far they are from their destination
* User should be able to cancel navigation and re-select location or section
* A server should host config details for locations so that users can easily navigate a new location

### functional requirements
* App should be able to know its position within +-2 meters of the actual position indoors
* App should be easily deployable to a new location through a config file
* Config file documented be clearly defined and documented
* 

## Challenges

* iBeacon distance accurracy
 * iBeacons have an accuracy of +- 1 Meter from within 10 meters, and worse if the device is > 10 meters away. This will present a challenge in getting accurate location data while using both triangulation and basic proximity based tracking.
 * To combat this an algorithm that averages the readings over time and then triangulates could be use, this is going to be one of the primary areas of research for the project
* Determining a viable method of providing feedback to users who are sight impaired
 * Audio feedback is an easy solution, but not preferred as it is disruptive
 * A better solution would be to develop a haptic feedback system where different vibration patterns mean different things (Left or Right)
