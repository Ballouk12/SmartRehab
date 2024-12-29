import React, { useEffect, useState } from "react";
import { Plus, Dumbbell, ChevronLeft, Edit, Trash } from 'lucide-react';
import ExerciseVideo from "./ExerciseVideo";

const ExerciseList = () => {
  const [exercises, setExercises] = useState([]);
  const [open, setOpen] = useState(false);
  const [delId, setDelId] = useState(0);
  const [isShowingOptions, setIsShowingOptions] = useState({});

  const handleOpen = () => setOpen(!open);

  useEffect(() => {
    const fetchExercises = async () => {
      try {
        const response = await fetch("http://localhost:8083/api/exercices/all", {
          method: "GET",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
        });

        if (!response.ok) throw new Error(`Erreur: ${response.status}`);
        const data = await response.json();
        setExercises(data);
      } catch (error) {
        console.error("Erreur lors de la récupération des exercices :", error);
      }
    };
    fetchExercises();
  }, []);
  
  const handleDelete = async (id) => {
    try {
      const response = await fetch(`http://localhost:8083/api/exercices/${id}`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
      });

      if (!response.ok) throw new Error(`Erreur lors de la suppression : ${response.status}`);

      setExercises((prev) => prev.filter((exercise) => exercise.exercice.id !== id));
      handleOpen();
    } catch (error) {
      console.error("Erreur lors de la suppression :", error);
    }
  };

  const handleEditExercise = (exerciseId) => {
    // Code pour modifier l'exercice
    console.log("Modification de l'exercice avec l'ID :", exerciseId);
  };

  const handleShowOptions = (exerciseId) => {
    setIsShowingOptions((prevState) => ({
      ...prevState,
      [exerciseId]: !prevState[exerciseId]
    }));
  };

  const beforeDel = (id) => {
    setDelId(id);
    handleOpen();
  }

  return (
    <>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        {exercises.map((exercise) => (
          <div key={exercise.exercice.id} className="bg-white shadow rounded-lg overflow-hidden">
            <div className="h-48 overflow-hidden">
              <img
                className="w-full h-full object-cover"
                src={`data:image/png;Base64,${exercise.image}`}
                alt="image"
              />
            </div>
            <div className="p-4 flex justify-between">
              <div>
                <h3 className="text-lg font-medium text-gray-900">
                  {exercise.exercice.nom}
                </h3>
                <p className="mt-2 text-sm text-gray-500">
                  {exercise.exercice.description}
                </p>
                {exercise.vedio && (
                  <ExerciseVideo videoBase64={exercise.vedio} />
                )}
              </div>
              <div className="relative">
                <button
                  onClick={() => handleShowOptions(exercise.exercice.id)}
                  className="text-gray-400 hover:text-gray-600 focus:outline-none transition-colors duration-200"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6">
                    <path fillRule="evenodd" d="M10.5 6a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0zm0 6a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0zm0 6a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0z" clipRule="evenodd" />
                  </svg>
                </button>
                {isShowingOptions[exercise.exercice.id] && (
                  <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg z-10">
                    <div className="py-1">
                      <button
                        onClick={() => handleEditExercise(exercise.exercice.id)}
                        className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900 transition-colors duration-200"
                      >
                        <Edit className="w-4 h-4 mr-2" />
                        Edit
                      </button>
                      <button
                        onClick={() => beforeDel(exercise.exercice.id)}
                        className="flex items-center px-4 py-2 text-sm text-red-600 hover:bg-gray-100 hover:text-red-700 transition-colors duration-200"
                      >
                        <Trash className="w-4 h-4 mr-2" />
                        Delete
                      </button>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>

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
                  onClick={() => handleDelete(delId)}
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
    </>
  );
};

export default ExerciseList;