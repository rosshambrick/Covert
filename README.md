Commandroid
===========

Simplify async operations in your Android apps by letting Commandroid perform your background work and report back when interesting things have happened.  By adhearing to priniciples of DDD and taking advantage of CQRS, Commandroid gives you a clean and testable model layer by seperating the concerns of making changes in your model and querying the state of your model.

### Features
* All non-UI work goes into POJO commands that are easily testable
* Never worry again about blocking the UI thread when doing disk or network I/O
* Simple and fast
* Can run commands either in parrellel or serial
* Configure number of threads.  [Defaults to single thread]
* EventBus for eventing
    * Bundled by default
    * Configure with your own EventBus instance
    * Handle data cacheing by using sticky events

### Dagger setup
```
    @Provides @Singleton
    DependencyInjector provideInjector() {
        return new DependencyInjector() {
            @Override
            public void inject(Object object) {
                mObjectGraph.inject(object);
            }
        };
    }

    @Provides @Singleton
    CommandProcessor provideCommandProcessor(DependencyInjector dependencyInjector) {
        return new ThreadPoolCommandProcessor(dependencyInjector);
    }

```


### Define a Command
```
public class UpdateAddress extends Command {
    @Inject ContactsRepository mLocalContactsRepository;
    @Inject RemoteContactsRepository mRemoteContactsRepository;
    
    private long mContactId;
    private String mNewAddress;

	public UpdateAddress(long contactId, String newAddress) {
		mContactId = contactId;
		mNewAddress = newAddress;
	}

    public void execute() {
        Contact contact = mContactsRepository.findById(mContactId);

		if (contact.getAddress().equals(mNewAddress)) {
			//nothing to do
			return;
		}
		
		contact.setAddress(mNewAddress);
		mRemoteContactsRepository.update(contact);
		mLocalContactsRepository.update(contact);
    }
}
```

### Run a Command from a Fragment
```
public void onSaveClicked() {
	String newAddress = mAddressTextView.getText().ToString();
	mCommandProcessor.send(new UpdateAddressCommand(mContactId, newAddress), this);
}
```

### Handle Result in a Fragment
```
public void commandComplete(UpdateAddress command) {
	Toast.makeText(this, "Address updated successfully", Toast.LENGTH_LONG).show();
}

public void commandFailed(CommandError event) {
    String commandName = event.getCommand().getClass().getSimpleName();
    Toast.makeText(getActivity(), commandName + " command failed", Toast.LENGTH_LONG).show();
}
```

### Run a Query from a Fragment
```
public void onResume() {
	mCommandProcessor.load(new ContactQuery(mContactId), this);
}
```

### Handle Query result in a Fragment
```
public void loadComplete(Contact contact) {
	display(contact);
}

public void loadFailed(ContactQuery query) {
    String queryName = query.getClass().getSimpleName();
    Toast.makeText(getActivity(), queryName + " query failed", Toast.LENGTH_LONG).show();
}
```