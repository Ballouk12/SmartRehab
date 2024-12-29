import React, { useEffect, useState } from "react";
import { Plus, Dumbbell, ChevronLeft, Edit, Trash } from 'lucide-react';

const SelectedPatient = ({selectedPatient, setSelectedPatient, setView, setProgId,updateItem ,setUpdateItem }) =>{
    const [isShowingOptions, setIsShowingOptions] = useState({});
    const [delId,setDelId] = useState(0)
      const [open, setOpen] = useState(false);
      const handleOpen = () =>(setOpen(!open))

    useEffect(() => {
        console.log("le patients selectioner", selectedPatient)
    }, [])

    if (!selectedPatient) {
        return (
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 bg-gray-50">
                <div className="text-center py-12 bg-white rounded-xl shadow">
                    <p className="text-gray-500 text-lg">No patient selected.</p>
                </div>
            </div>
        );
    }

    const handleProgramClick = (program) => {
        setProgId(program?.id);
        setView("exercisesp");
    }

    const handleShowOptions = (programId) => {
        setIsShowingOptions((prevState) => ({
            ...prevState,
            [programId]: !prevState[programId]
        }));
    }

    const beforeDel = (id) => {
        setDelId(id);
        handleOpen();
    }

    const onDeleteSuccess = (deletedId) => {
        setSelectedPatient({
          ...selectedPatient, 
          programs: selectedPatient.programs.filter(program => program.id !== deletedId)
        });
      };
      
    const handleDeleteProgram = async(programId) => {
      const url = `http://localhost:8083/api/programs/${programId}`; // Remplacez par l'URL de votre API
  
      try {
        const response = await fetch(url, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
          },
        });
  
        if (response.ok) {
          if (onDeleteSuccess) onDeleteSuccess(programId); 
          handleOpen();
        }
      } catch (error) {
        console.error("Erreur lors de la suppression :", error);
      }
    };

    const handleUpdate = (item) => {
        setUpdateItem(item);
        setView("progUpdate");
    }

    return (
        <>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 bg-gray-50">
            <div className="space-y-8">
                <div className="bg-white rounded-xl shadow-lg p-6">
                    <div className="flex justify-between items-center">
                        <div className="space-y-1">
                            <h2 className="text-3xl font-bold text-gray-900">
                                {selectedPatient?.prenom || ''} {selectedPatient?.nom || ''}
                            </h2>
                            <p className="text-lg text-blue-600">{selectedPatient?.email || ''}</p>
                        </div>
                        <div className="flex gap-4">
                            <button
                                onClick={() => setSelectedPatient(null)}
                                className="flex items-center px-4 py-2 text-sm font-medium rounded-lg border border-gray-300 text-gray-700 bg-white hover:bg-gray-50 transition-colors duration-200"
                            >
                                <ChevronLeft className="w-4 h-4 mr-2" />
                                Back
                            </button>
                            <button
                                onClick={() => setView('newProgram')}
                                className="flex items-center px-4 py-2 text-sm font-medium rounded-lg bg-blue-600 text-white hover:bg-blue-700 transition-colors duration-200"
                            >
                                <Plus className="w-4 h-4 mr-2" />
                                Add Program
                            </button>
                        </div>
                    </div>
                </div>

                <div className="grid gap-6">
                    {selectedPatient?.programs?.length > 0 ? (
                        selectedPatient.programs.map((program) => (
                            <div
                                key={program?.id || Math.random()}
                                className="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-200 cursor-pointer"
                            >
                                <div className="p-6 flex justify-between items-center">
                                    <div>
                                        <h3 className="text-xl font-semibold text-gray-900">
                                            {program?.name || 'Untitled Program'}
                                        </h3>
                                        <p className="mt-2 text-gray-600">
                                            {program?.description || 'No description available'}
                                        </p>
                                    </div>
                                    <div className="relative">
                                        <button
                                            onClick={() => handleShowOptions(program.id)}
                                            className="text-gray-400 hover:text-gray-600 focus:outline-none transition-colors duration-200"
                                        >
                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6">
                                                <path fillRule="evenodd" d="M10.5 6a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0zm0 6a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0zm0 6a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0z" clipRule="evenodd" />
                                            </svg>
                                        </button>
                                        {isShowingOptions[program.id] && (
                                            <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg z-10">
                                                <div className="py-1">
                                                    <button
                                                        onClick={() => handleUpdate(program)}
                                                        className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900 transition-colors duration-200"
                                                    >
                                                        <Edit className="w-4 h-4 mr-2" />
                                                        Edit
                                                    </button>
                                                    <button
                                                        onClick={() => beforeDel(program.id)}
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
                                <div className="border-t border-gray-100 p-6">
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                        {program?.exercises?.length > 0 ? (
                                            program.exercises.map((exercise) => (
                                                <div 
                                                    key={exercise?.id || Math.random()} 
                                                    className="bg-gray-50 rounded-lg p-4 hover:bg-gray-100 transition-colors duration-200"
                                                    onClick={() => handleProgramClick(program)}
                                                >
                                                    <div className="flex items-center gap-2 mb-2">
                                                        <Dumbbell className="w-4 h-4 text-blue-600" />
                                                        <h4 className="font-medium text-gray-900">
                                                            {exercise?.exercice?.nom || 'Unnamed Exercise'}
                                                        </h4>
                                                    </div>
                                                    <ul className="space-y-2">
                                                        {Object.entries(exercise?.inputs || {}).map(([key, value]) => (
                                                            <li key={key} className="text-sm text-gray-600">
                                                                <span className="font-medium">{key}:</span> {value}
                                                            </li>
                                                        ))}
                                                    </ul>
                                                </div>
                                            ))
                                        ) : (
                                            <p className="text-gray-500 italic">No exercises available.</p>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))
                    ) : (
                        <div className="text-center py-12 bg-white rounded-xl shadow">
                            <p className="text-gray-500 text-lg">No programs available yet.</p>
                        </div>
                    )}
                </div>
            </div>
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
                  onClick={() => handleDeleteProgram(delId)}
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
}
export default SelectedPatient;