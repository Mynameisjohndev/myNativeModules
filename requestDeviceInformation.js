import { Platform, NativeModules, PermissionsAndroid } from "react-native";
import { formatBytes } from "./formatBytes";

const { DeviceStorage } = NativeModules;

const requestDeviceInformation = () => {
  if (Platform.OS === "android") {
    return new Promise(async (resolve) => {
      const permission = await DeviceStorage.checkReadPermissionReadExternalStorage();
      const { DFESPACOTOTAL } = await DeviceStorage.getTotalStorage();
      const { DFESPACOUTILIZADO } = await DeviceStorage.getUsedStorage();
      if (permission === "granted") {
        console.log({
          DFESPACOTOTAL: formatBytes(DFESPACOTOTAL),
          DFESPACOUTILIZADO: formatBytes(DFESPACOUTILIZADO)
        })
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

async function requestStoragePermission() {
  try {
    const status = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE);
    console.log(status);
  } catch (err) {
    console.warn(err);
  }
}

export { requestDeviceInformation }