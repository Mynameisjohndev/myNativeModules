import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { StyleSheet, Text, View, NativeModules, Button } from 'react-native';
import DeviceInfo from 'react-native-device-info';
const { DeviceStorage } = NativeModules;
import { convertHzToMhz } from "./convertHzToMhz"
import { PermissionsAndroid } from 'react-native';
import { requestDeviceInformation } from './requestDeviceInformation';

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

const checkDate = async () => {
  return new Promise(async (resolve) => {
    DeviceStorage.getInstallationTime()
      .then(installationTime => {
        let data = new Date(installationTime.date);
        console.log(data);
      })
      .catch(error => {
        console.error('Erro ao obter a data e hora de instalação:', error);
      });
  })
}

const checkMemory = () => {
  return new Promise(async (resolve) => {
    DeviceStorage.getDeviceRAM().then(totalMemory => {
      console.log('Total memory:', totalMemory);
    });
  })
}

const getDeviceCPUInfo = () => {
  return new Promise(async (resolve) => {
    DeviceStorage.getCPUFrequency().then(totalMemory => {
      console.log(convertHzToMhz (totalMemory));
    });
  })
}

const getDeviceSOVersion = () => {
  return new Promise(async (resolve) => {
    DeviceStorage.getDeviceModel().then(device => {
      console.log(device);
    });
    DeviceStorage.getSystemVersion().then(device => {
      console.log(device);
    });
  })
}

const getHu = () => {
  return new Promise(async (resolve) => {
    DeviceStorage.checkReadPermissionReadExternalStorage().then(device => {
      console.log(device);
    });
  })
}



export default function App() {
  return (
    <View style={styles.container}>
      <Text>Open up App.js to start working on your app!</Text>
      <StatusBar style="auto" />
      <Button title='Checar' onPress={requestDeviceInformation} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
