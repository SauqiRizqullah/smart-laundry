import {
  Text,
  View,
  TextInput,
  TouchableOpacity,
  ScrollView,
  Image,
} from "react-native";
import { useState } from "react";
import { Link, router } from "expo-router";
import { SafeAreaView } from "react-native-safe-area-context";
import { FormField } from "../../components";
import { images } from "../../constants";

const signIn = () => {
  const [signInForm, setSignInForm] = useState({
    email: "",
    password: "",
  });
  return (
    // <SafeAreaView className="bg-white">
      <ScrollView className="bg-secondary-300 w-full">
        <View className="h-full w-full flex items-center ">
          <View className="pl-6 w-full h-32 flex justify-center items-center">
            <Image
              source={images.logo}
              className="w-[250px] h-[250px]"
              resizeMode="contain"
            />
          </View>
          <View className="flex h-full w-full items-center px-5 bg-white shadow-md shadow-black-200 rounded-3xl">
            <FormField
              title="Email"
              placeholder="yourmail@example.com"
              value={signInForm.email}
              handleChangeText={(e) =>
                setSignInForm({ ...signInForm, email: e })
              }
              keyboardType="email-address"
              otherStyles="mt-3"
            />
            <FormField
              title="Password"
              placeholder="Password"
              value={signInForm.password}
              handleChangeText={(e) =>
                setSignInForm({ ...signInForm, password: e })
              }
              otherStyles="mt-2"
            />
            <View className="flex items-center w-full h-14 my-5">
              <TouchableOpacity
                className="bg-secondary-300 shadow shadow-secondary-300 w-full rounded-xl h-full flex items-center justify-center"
                onPress={() => router.push("/home")}
              >
                <Text className="text-white font-bold">Sign In</Text>
              </TouchableOpacity>
            </View>
            <View className="h-64 w-full flex items-end justify-end">
              <Text className="text-gray-500 text-base">
                Don't have account?
              </Text>
              <Link href="/sign-up" className="font-pbold text-secondary-300">
                Sign Up
              </Link>
            </View>
          </View>
        </View>
      </ScrollView>
    // </SafeAreaView>
  );
};

export default signIn;
