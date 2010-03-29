package org.objectweb.proactive.core.component.componentcontroller.monitoring;

import org.objectweb.proactive.core.UniqueID;

public interface EventControl {

	void start();
	
	void setBodyToMonitor(UniqueID objectID, String runtimeURL, String componentName);
	
	
}
