import React, { useState, useEffect } from 'react';
import { Plus, Dumbbell } from 'lucide-react';
import { PatientForm } from './components/PatientForm';
import { ExerciseForm } from './components/ExerciseForm';
import { ProgramForm } from './components/ProgramForm';
import { PatientList } from './components/PatientList';
import SelectedPatient from './components/SelectedPatient';
import ExercieList from './components/ExercicesList';
import NavBar from './components/NavBar';
import ExerciesProgramList from './components/ExercicesProgramList';
import { PatientUpdate } from './components/PatientUpdate';
import { ProgramUpdate } from './components/ProgramUpdate';
import Login from './components/Login';
import Register from './components/Register';
function App() {
  const [view, setView] = useState('patients');
  const [exercises, setExercises] = useState([]);
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [updatePat,setUpdatePat] = useState(null)
  const [updateProg,setUpdateProg] = useState(null)
  const [progId,setProgId] = useState(0);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [authView, setAuthView] = useState('login'); // 'login' or 'register'

  if (!isAuthenticated) {
    return authView === 'login' 
      ? <Login 
      setIsAuthenticated={setIsAuthenticated}
        onSwitchToRegister={() => setAuthView('register')} 
        />
      : <Register 
          onSwitchToLogin={() => setAuthView('login')} 
        />;
  }

  return (
    <div className="min-h-screen bg-gray-100">
    <NavBar setView={setView}  view={view}/>
      <main className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-8">
        {view === 'patients' && !selectedPatient && (
          <PatientList
                      onSelectPatient={setSelectedPatient}
          setUpdateItem={setUpdatePat} setView={setView}/>
        )}

        {view === 'patients' && selectedPatient && (
          <SelectedPatient selectedPatient={selectedPatient}  setView={setView} setProgId={setProgId} setSelectedPatient={setSelectedPatient} updateItem ={updateProg} setUpdateItem={setUpdateProg}/>
        )}

        {view === 'exercises' && (
          <ExercieList />
        )}

        {view === 'newPatient' && (
                <PatientForm />
        )}

        
        {view === 'upPatient' && (
                <PatientUpdate updateItem={updatePat} setUpdateItem={setUpdatePat}/>
        )}

        {view === 'newExercise' && (
          
                <ExerciseForm />
        )}

        {view === 'newProgram' && selectedPatient && (
          
                <ProgramForm selectedPatient={selectedPatient}/>
        )}

{view === 'exercisesp' && (
          <ExerciesProgramList programId={progId}/>
        )}
        
{view === 'progUpdate' && (
          <ProgramUpdate selectedPatient={selectedPatient} updateItem={updateProg}/>
        )}
      </main>
    </div>
  );
}

export default App;