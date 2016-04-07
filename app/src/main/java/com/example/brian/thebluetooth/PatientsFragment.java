package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class PatientsFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GridView gView = (GridView) inflater.inflate(R.layout.content_patient, container, false);
        gView.setAdapter(new PatientImageAdapter(getActivity()));

        // set the listeners for the images
        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                final Intent i = new Intent(.this, ViewImage.class);
                Fragment fragment = new PatientPage();
                Bundle bun = new Bundle();
                switch (position) {
                    case 0:
                        bun.putFloat("Longitude", (float) -123.2);
                        bun.putFloat("Latitude", (float) 49.26);
                        bun.putString("Name", "Donald Trump");
                        bun.putString("Address", "123 Fake Street");
                        bun.putString("Phone", "666-666-6666");
                        bun.putString("Med", "Patient suffers from dementia and late stage Alzheimer's Disease. Needs constant medical attention and supervision.");


                        break;
                    case 1:
                        bun.putFloat("Longitude", (float) -0.124851);
                        bun.putFloat("Latitude", (float) 51.500756);
                        bun.putString("Name", "Margaret Thatcher");
                        bun.putString("Address", "Previously #10 Downing St., London");
                        bun.putString("Phone", "1534123");
                        bun.putString("Med", "Recently deceased. Died of natural causes at the age of 87.");

                        break;
                    case 2:
                        bun.putFloat("Longitude", (float) -123.1038);
                        bun.putFloat("Latitude", (float) 49.2734);
                        bun.putString("Name", "George Clooney");
                        bun.putString("Address", "Beverly Hills, California");
                        bun.putString("Phone", "5104513");
                        bun.putString("Med", "Patient suffers from the disease of being a terrible actor, which directly affects his mental state. Keep under close observation.");


                        break;
                    case 3:
                        bun.putFloat("Longitude", (float) -123.0919);
                        bun.putFloat("Latitude", (float) 49.2840);
                        bun.putString("Name", "Jake Gyllenhaal");
                        bun.putString("Address", "Beverly Hills, California");
                        bun.putString("Phone", "435245234");
                        bun.putString("Med", "Patient suffers from early onset Alzhemier's Disease. The disease is in the early stages of neurological degeneration and he is currently under observation.");


                        break;
                    case 4:
                        bun.putFloat("Longitude", (float) -123.1417);
                        bun.putFloat("Latitude", (float) 49.3017);
                        bun.putString("Name", "Audrey Hepburn");
                        bun.putString("Address", "Hollywood Boulevard, California");
                        bun.putString("Phone", "325235245");
                        bun.putString("Med", "Deceased: Patient suffered from Appendiceal Cancer prior to her death.");
                        break;
                    case 5:
                        bun.putFloat("Longitude", (float) -122.8011);
                        bun.putFloat("Latitude", (float) 49.1044);
                        bun.putString("Name", "Tupac Shakur");
                        bun.putString("Address", "Somewhere secret");
                        bun.putString("Phone", "5253255443");
                        bun.putString("Med", "Currently locked up in the high security ward, for his own protection.");


                        break;
                    case 6:
                        bun.putFloat("Longitude", (float) -123.04032);
                        bun.putFloat("Latitude", (float) 49.28271);
                        bun.putString("Name", "Ronald Reagan");
                        bun.putString("Address", "Reagan Presidental Library, Washington");
                        bun.putString("Phone", "5253233643");
                        bun.putString("Med", "Deceased: Death due to Alzheimer's Disease and natural causes associated with old age.");


                        break;
                    case 7:
                        bun.putFloat("Longitude", (float) -123.11737);
                        bun.putFloat("Latitude", (float) 49.34253);
                        bun.putString("Name", "Hillary Clinton");
                        bun.putString("Address", "Classifed By The Secret Service");
                        bun.putString("Phone", "1153263643");
                        bun.putString("Med", "You are now on the FBI Watch List for requesting this information.");


                        break;
                    default:
                        break;
                }

                fragment.setArguments(bun);
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });

        return gView;
    }
}
