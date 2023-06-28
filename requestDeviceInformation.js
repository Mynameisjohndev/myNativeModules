import { Platform, NativeModules, PermissionsAndroid } from "react-native";
import { formatBytes } from "./formatBytes";
import { convertHzToMhz } from "./convertHzToMhz";

const { DeviceStorage } = NativeModules;



async function requestStoragePermission() {
  try {
    const status = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE);
    return status;
  } catch (err) {
    return "err"
  }
}

const requestDeviceInformation = () => {
  if (Platform.OS === "android") {
    return new Promise(async (resolve) => {
      const permission = await requestStoragePermission();
      const { DFESPACOTOTAL } = await DeviceStorage.getTotalStorage();
      const { DFESPACOUTILIZADO } = await DeviceStorage.getUsedStorage();
      const { DFDATAINSTALACAO, DFHORAINSTALACAO } = await DeviceStorage.getInstallationTime();
      const ram = await DeviceStorage.getDeviceRAM();
      const cpu = await DeviceStorage.getCPUFrequency();
      const DFVERSAOSO = await DeviceStorage.getSystemVersion();
      const DFMODELOAPARELHO = await DeviceStorage.getDeviceModel();
      const DFIMEI = await DeviceStorage.getUniqueIdSync();
      if (permission === "granted") {
        const deviceInfo = {
          DFESPACOTOTAL: formatBytes(DFESPACOTOTAL),
          DFESPACOUTILIZADO: formatBytes(DFESPACOUTILIZADO),
          DFDATAINSTALACAO,
          DFHORAINSTALACAO,
          DFMEMORIARAM: formatBytes(ram),
          DFCPU: convertHzToMhz(cpu),
          DFSO: "Android",
          DFVERSAOSO,
          DFMODELOAPARELHO,
          DFIMEI
        };
        resolve(deviceInfo);
        return deviceInfo;
      }
    })
  }
  if (Platform.OS === "ios") {
    // Possível implemtação futura
  }
}

const checkStorage = async () => {
  return new Promise(async (resolve) => {
    const getTotalStorage = await DeviceStorage.getTotalStorage();
    const getUsedStorage = await DeviceStorage.getUsedStorage();
    const dateInstalation = await DeviceStorage.getInstallationTime();
    console.log({
      used: getTotalStorage.total - getUsedStorage.total, total: getTotalStorage.total,
      dateInstalation
    })
  })
}


export { requestDeviceInformation }