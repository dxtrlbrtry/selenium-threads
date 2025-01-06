## Device farm options

### In-house devices

The most convenient option is running tests on in-house physical devices.

#### Pros:

- Has full capabilities of a phisical device
- Can be configured for the needs of the project
- Fast communication with Appium server
- Easy to debug via access to logs and remote control
- Can persist session throughout tests to reduce downtime

#### Cons:

- Expensive up-front cost
- Key device models change over time
- Requires knowledge of setting up device so it doesn't interfere or block test execution
- Electricity cost for constant uptime of the devices, and the connected host(s)

### 3rd party device farm

Second most convenient option is running tests on 3rd party physical devices.

#### Pros:

- Has most capabilities of a physical device
- No device configuration needed
- Device queue and availability is handled by the provider

#### Cons:

- Most expensive option
- Slow communication with Appium server
- Some capabilities might not be available due to security or other reasons, such as direct adb/xcode commands to the device.
- Capability to use device camera might be limited
- No option to persist session throughout tests

### Emulated devices

The least convenient option is running on emulators

#### Pros:

- Easy to configure
- Very low cost
- No physical device needed for local execution
- Easy access to a large variety of devices

#### Cons:

- Emulating iOS requires a Mac as host
- Very limited capabilities. Not able to use systems such as camera, services using device uuid
- Slower than physical devices
- Can give inconsistent results
- For regular usage, several scripts need to be written that automatically prepare the device for test execution, such as launch/shut down, resetting device, media upload

## Device farm cost

### 3rd party provider

The yearly fee of using a 3rd party device provider with 5 devices is around $15.000.

### In-house physical device

Purchasing physical devices is an up-front cost when setting up an in-house device farm, which may cost the company up to $8000 if purchasing recent device models.</br>This includes 5 iOS devices from low to high end and 5 Android devices from low to high end.
Buying recent models is recommended since those devices will be relevant for multiple years. The devices should include also low-end, mid-end and high-end devices to have high coverage on compatibliity. While it is not always needed to test on low-end devices, doing so might provide insight on the app's performance on these devices.</br>
For the iOS devices, a Mac is also needed to set up communication between the test runner, the appium server and the device, that may cost up to $2000.</br>
For android devices, the hosts operating system is of no importance, as windows, macos and linux can all run adb server. The most optimal version would be to plug in the android devices to a bare-metal linux machine that is accessible by Jenkins</br>
After an up-front investment of ~$10.000, additional costs may arise when purchasing newer generation devices over the years.</br>
Besides the up-front cost, the maintenance cost adds up when an employee spends time setting up, troubleshooting or monitoring the state of the devices. While this doesn't require full-time attention of an employee in case of a farm of 10 devices, it will take up 5 to 10% of somebody's time throughout the year.
In case there is no experienced person that can set up such a device farm, costs may further rise considering the time it may take to train or hire such a person.
Considering all this and the electricity consumption, a gross estimate would be around $2000 per year

### Emulated devices

The cost of running a device farm consisting of emulated device consists mainly of the price of the host machine emulating the devices, and the electricity cost of keeping them running.</br>
Also existing host machines can be used as resource to emulate devices. Depending on the power of the host, an average macbook can emulate 2-3 devices at a time without compromising the quality of any emulator. Keeping 3 hosts available with a pool of 10 devices will cost approximately $1000 per year.

### Summary

- 3rd Party provider: $15.000 / year, $0 up-front
- In-house device farm: $2000 / year, $10000 up-front
- Emulated devices: $1000 per year

## In-house device farm as a service

To enable multiple teams to access the device farm, it has to be ensured that only 1 test run targets a device at a time. For this a server managing the device states globally can be set up.

#### Option 1

Before a test run, the runner communicates to the server the number of requested devices, and waits for a determined amount of time until the target number of devices is available. After the test run is finished, the devices are released. This approach will result in similar execution times for the same suite.

#### Option 2

Each test case is waiting for the first available device, and the device is released after each test case. This approach may result in different execution times for the same suite if multiple suites are queuing for the device pool.

#### Option 3

While this option is not exclusive to the other options, the best way to ensure not interfering with other teams' testing processes is communicating and aggreeing on the schedule the devices are used to run tests.
