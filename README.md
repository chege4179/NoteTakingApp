# Note Taking App

This is a simple note Taking app that allows CRUD operations on your notes

The notes are also stored on firebase firestore for remote storage capabilities to enable
users to retrieve notes across devices.

The app has email/password auth which uses Firebase Auth for simplicity instead of making a 
custom backend for it

The goal of this project is show how sync data between local cache and remote database

For the remote Database, I am using firebase Firestore and the local cache is backed by a room database

The syncing strategy is not perfect and is still a WIP 




