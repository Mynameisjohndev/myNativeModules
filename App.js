import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { StyleSheet, Text, View, NativeModules, Button } from 'react-native';
const { DeviceStorage } = NativeModules;


const checkStorage = async () => {
  return new Promise(async (resolve) =>{
    const getTotalStorage = await DeviceStorage.getTotalStorage();
    const getUsedStorage = await DeviceStorage.getUsedStorage();
    const dateInstalation = await DeviceStorage.getInstallationTime();
    console.log({
      used:getTotalStorage.total - getUsedStorage.total, total:getTotalStorage.total,
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
    DeviceStorage.getDeviceCPUInfo().then(totalMemory => {
      console.log('Total memory:', totalMemory);
    });
  })
}


export default function App() {
  return (
    <View style={styles.container}>
      <Text>Open up App.js to start working on your app!</Text>
      <StatusBar style="auto" />
      <Button title='Checar' onPress={getDeviceCPUInfo} />
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
