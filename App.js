import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { StyleSheet, Text, View, NativeModules, Button } from 'react-native';
import DeviceInfo from "react-native-device-info";
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

export default function App() {
  return (
    <View style={styles.container}>
      <Text>Open up App.js to start working on your app!</Text>
      <StatusBar style="auto" />
      <Button title='Checar' onPress={checkStorage}/>
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
