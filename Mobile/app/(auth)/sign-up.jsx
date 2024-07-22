import {
  Text,
  View,
  TextInput,
  TouchableOpacity,
  ScrollView,
  KeyboardAvoidingView,
  Platform,
} from "react-native";
import { useState, useRef } from "react";
import { Link, router } from "expo-router";
import { SafeAreaView } from "react-native-safe-area-context";
import { FormField } from "../../components";
const signUp = () => {
  const [signUpForm, setSignUpForm] = useState({
    name: "",
    address: "",
    phoneNo: "",
    email: "",
    password: "",
  });

  const scrollViewRef = useRef(null);

  const scrollToInput = (ref) => {
    setTimeout(() => {
      scrollViewRef.current.scrollTo({
        y: ref,
        animated: true,
      });
    }, 100);
  };

  return (
    // <SafeAreaView className="bg-white h-full">
    <KeyboardAvoidingView
      behavior={Platform.OS === "ios" ? "padding" : "height"}
      className="flex-1"
    >
      <ScrollView className="bg-secondary-300 w-full flex-1"
          ref={scrollViewRef}
          contentContainerStyle={{ flexGrow: 1 }}>
        <View className="h-full w-full flex items-center ">
          <View className="pl-6 w-full h-32 flex justify-center">
          <Text className="text-white text-2xl font-pbold">Create </Text> 
            <Text className="text-primary text-3xl font-pextrabold">
              Your Account
              </Text>
          </View>
          <View className="flex h-full w-full items-center px-5 bg-white shadow-md shadow-black-200 rounded-3xl">
            <FormField
              title="Name"
              placeholder="John Doe"
              value={signUpForm.name}
              handleChangeText={(e) =>
                setSignUpForm({ ...signUpForm, name: e })
              }
              otherStyles="mt-3"
            />
            <FormField
              title="Address"
              placeholder="Melati 59, Ragunan, South Jakarta"
              value={signUpForm.address}
              handleChangeText={(e) =>
                setSignUpForm({ ...signUpForm, address: e })
              }
              otherStyles="mt-1"
            />
            <FormField
              title="Phone No"
              placeholder="081234567890"
              value={signUpForm.phoneNo}
              handleChangeText={(e) =>
                setSignUpForm({ ...signUpForm, phoneNo: e })
              }
              onFocus={() => scrollToInput(50)}
              keyboardType="phone-pad"
              otherStyles="mt-1"
            />
            <FormField
              title="Email"
              placeholder="yourmail@example.com"
              value={signUpForm.email}
              handleChangeText={(e) =>
                setSignUpForm({ ...signUpForm, email: e })
              }
              onFocus={() => scrollToInput(300)}
              keyboardType="email-address"
              otherStyles="mt-1"
            />
            <FormField
              title="Password"
              placeholder="Password"
              value={signUpForm.password}
              handleChangeText={(e) =>
                setSignUpForm({ ...signUpForm, password: e })
              }
              onFocus={() => scrollToInput(300)}
              otherStyles="mt-1"
            />
            <View className="flex items-center w-full h-14 my-5 ">
              <TouchableOpacity
                className="bg-secondary-300 shadow shadow-secondary-300 w-full rounded-xl h-full flex items-center justify-center"
                onPress={() => router.push("/home")}
              >
                <Text className="text-white font-bold">Sign Up</Text>
              </TouchableOpacity>
            </View>
            <View className="w-full flex items-end justify-end mb-8">
              <Text className="text-gray-500 text-base">
                Already have account?
              </Text>
              <Link href="/sign-in" className="font-pbold text-secondary-300">
                Sign In
              </Link>
            </View>
          </View>
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
    // </SafeAreaView>
  );
};

export default signUp;
