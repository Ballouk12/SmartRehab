import React, { useEffect, useState } from 'react';
import { Users } from 'lucide-react';
import { select } from '@material-tailwind/react';

export function PatientList({onSelectPatient ,setView ,setUpdateItem}) {

  const [patients, setPatients] = useState([]);
  const [patientId,setPatientId] = useState();
  const [open,setOpen] = useState(false)
  const handleOpen = () => (setOpen(!open)) ;

  useEffect(() => {
      // Méthode pour récupérer tous les patients
      const fetchPatients = async () => {
          try {
            const response = await fetch("http://localhost:8083/api/patients/all", {
              method: "GET",
              headers: {
                  "Content-Type": "application/json"
              },
              credentials: "include" // Si vous travaillez avec des cookies pour l'authentification
          });
              if (!response.ok) {
                  throw new Error("Erreur lors de la récupération des patients");
              }

              const data = await response.json();
              setPatients(data);
          } catch (err) {
              console.log("erreur lors de la recuperation des patients ballouk",err.message);
          } 
      };

      fetchPatients();
  }, []);

  const onDeleteSuccess = (deletedId) => {
    setPatients(patients.filter(patient => patient.id !== deletedId));
  };

  const deletePatient = async () => {
    const url = `http://localhost:8083/api/patients/delete/${patientId}`; // Remplacez avec votre URL d'API

    try {
      const response = await fetch(url, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        if (onDeleteSuccess) onDeleteSuccess(patientId); // Met à jour l'état après la suppression
        handleOpen();
      } else {
        alert("Échec de la suppression du patient");
      }
    } catch (error) {
      console.error("Erreur lors de la suppression du patient :", error);
      alert("Erreur Resau ou erreur Serveur");
    }
  };

  const beforeDel = (id) => {
    setPatientId(id);
    handleOpen();
  }

  const handleUpdate = (upItem) => {
    setUpdateItem(upItem);
    setView("upPatient");
  }

  if (patients.length === 0) {
    return (
      <div className="text-center py-12">
        <Users className="mx-auto h-12 w-12 text-gray-400" />
        <h3 className="mt-2 text-sm font-medium text-gray-900">No patients</h3>
        <p className="mt-1 text-sm text-gray-500">Get started by creating a new patient.</p>
      </div>
    );
  }

  return (
    <div className="overflow-hidden bg-white shadow sm:rounded-md">
      <ul className="divide-y divide-gray-200">
        {patients.map((patient) => (
          <li key={patient.id} className='flex items-center justify-between px-4 py-4 sm:px-6'>
            <button
              onClick={() => onSelectPatient(patient)}
              className="block hover:bg-gray-50 w-full text-left"
            >
              <div className="px-4 py-4 sm:px-6">
                <div className="flex items-center justify-between">
                  <p className="truncate text-sm font-medium text-blue-600">
                    {patient.prenom} {patient.nom}
                  </p>
                  <div className="ml-2 flex flex-shrink-0">
                    <p className="inline-flex rounded-full bg-green-100 px-2 text-xs font-semibold leading-5 text-green-800">
                      1 programs
                    </p>
                  </div>
                </div>
                <div className="mt-2 sm:flex sm:justify-between">
                  <div className="sm:flex">
                    <p className="flex items-center text-sm text-gray-500">
                      {patient.email}
                    </p>
                  </div>
                  <div className="mt-2 flex items-center text-sm text-gray-500 sm:mt-0">
                    <p>{patient.login}</p>
                  </div>
                </div>
              </div>
            </button>
            <div className="flex items-center gap-2 ml-7">
                <button
                  type="button"
                  className="text-red-600 hover:text-red-800 transition-colors"
                  onClick={() => beforeDel(patient.id)}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth={1.5}
                    stroke="currentColor"
                    className="h-6 w-6"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0"
                    />
                  </svg>
                </button>
                <button
                  type="button"
                  className="text-green-600 hover:text-green-800 transition-colors"
                  onClick={() => handleUpdate(patient)}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth={1.5}
                    stroke="currentColor"
                    className="h-6 w-6"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10"
                    />
                  </svg>
                </button>
              </div>
          </li>
        ))}
      </ul>
      {open && (
        <div className="fixed inset-0 z-50 overflow-y-auto">
          <div className="flex min-h-full items-center justify-center p-4 text-center">
            <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
            <div className="relative transform overflow-hidden rounded-lg bg-white text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg">
              <div className="bg-white px-4 pb-4 pt-5 sm:p-6 sm:pb-4">
                <div className="sm:flex sm:items-start">
                  <div className="mx-auto flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10">
                    <svg className="h-6 w-6 text-red-600" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z" />
                    </svg>
                  </div>
                  <div className="mt-3 text-center sm:ml-4 sm:mt-0 sm:text-left">
                    <h3 className="text-lg font-semibold leading-6 text-gray-900">
                      Confirmer la suppression
                    </h3>
                    <div className="mt-2">
                      <p className="text-sm text-gray-500">
                        Êtes-vous sûr de vouloir supprimer cet exercice ?
                      </p>
                    </div>
                  </div>
                </div>
              </div>
              <div className="bg-gray-50 px-4 py-3 sm:flex sm:flex-row-reverse sm:px-6">
                <button
                  type="button"
                  onClick={() => deletePatient(patientId)}
                  className="inline-flex w-full justify-center rounded-md bg-red-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-red-500 sm:ml-3 sm:w-auto"
                >
                  Supprimer
                </button>
                <button
                  type="button"
                  onClick={handleOpen}
                  className="mt-3 inline-flex w-full justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 sm:mt-0 sm:w-auto"
                >
                  Annuler
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}