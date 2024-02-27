package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User(name, mobile);
        users.add(user);
        creatorPlaylistMap.put(user, null);
        userPlaylistMap.put(user, new ArrayList<>());
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        artists.add(artist);
        artistAlbumMap.put(artist, new ArrayList<>());
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist = null;
        for(Artist a: artists){
            if(a.getName().equals(artistName)){
                artist = a;
                break;
            }
        }

        if(artist == null){
            artist = createArtist(artistName);
        }

        Album album = new Album(title);
        albums.add(album);
        List<Album> albumList = artistAlbumMap.get(artist);
        if(albumList == null){
            albumList = new ArrayList<>();
        }
        albumList.add(album);
        artistAlbumMap.put(artist, albumList);
        albumSongMap.put(album, new ArrayList<>());
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Album album = null;

        for(Album a: albums){
            if(a.getTitle().equals(albumName)){
                album = a;
                break;
            }
        }

        if(album == null){
            throw new Exception("Album does not exist");
        }
        Song song = new Song(title, length);
        songs.add(song);
        List<Song> songList = albumSongMap.get(album);

        if(songList == null){
            songList = new ArrayList<>();
        }
        songList.add(song);
        albumSongMap.put(album, songList);
        songLikeMap.put(song, new ArrayList<>());
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User user = null;

        for(User u: users){
            if(u.getMobile().equals(mobile)){
                user = u;
                break;
            }
        }

        if(user == null){
            throw new Exception("User does not exist");
        }

        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        List<Song> selectedSongs = new ArrayList<>();
        for(Song song: songs){
            if(song.getLength() == length){
                selectedSongs.add(song);
            }
        }

        playlistSongMap.put(playlist, selectedSongs);

        List<Playlist> playlists = userPlaylistMap.get(user);
        if(playlists == null){
            playlists = new ArrayList<>();
        }
        playlists.add(playlist);
        userPlaylistMap.put(user, playlists);

        List<User> listeners = new ArrayList<>();
        listeners.add(user);
        playlistListenerMap.put(playlist, listeners);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User user = null;

        for(User u: users){
            if(u.getMobile().equals(mobile)){
                user = u;
                break;
            }
        }

        if(user == null){
            throw new Exception("User does not exist");
        }

        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        List<Song> selectedSongs = new ArrayList<>();
        for(Song song: songs){
            for(String songTitle: songTitles){
                if(song.getTitle().equals(songTitle)){
                    selectedSongs.add(song);
                }
            }
        }

        playlistSongMap.put(playlist, selectedSongs);

        List<Playlist> playlists = userPlaylistMap.get(user);
        if(playlists == null){
            playlists = new ArrayList<>();
        }
        playlists.add(playlist);
        userPlaylistMap.put(user, playlists);

        List<User> listeners = new ArrayList<>();
        listeners.add(user);
        playlistListenerMap.put(playlist, listeners);

        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User user = null;

        for(User u: users){
            if(u.getMobile().equals(mobile)){
                user = u;
                break;
            }
        }

        if(user == null){
            throw new Exception("User does not exist");
        }

        Playlist playlist = null;
        for(Playlist p: playlists){
            if(p.getTitle().equals(playlistTitle)){
                playlist = p;
                break;
            }
        }

        if(playlist == null){
            throw new Exception("Playlist does not exist");
        }

        if(!playlistListenerMap.get(playlist).contains(user)){
            List<User> userList = playlistListenerMap.get(playlist);
            userList.add(user);
            playlistListenerMap.put(playlist, userList);
            List<Playlist> playlists1 = userPlaylistMap.get(user);
            playlists1.add(playlist);
            userPlaylistMap.put(user, playlists1);
        }
        return playlist;
    }

    public void removeSong(String title) {
        Song song  = null;
        List<Song> updatedSongs = new ArrayList<>();
        for(Song s: songs){
            if(s.getTitle().equals(title)){
                song = s;
            }
            else{
                updatedSongs.add(s);
            }
        }
        songs = updatedSongs;

        for(Album a: albumSongMap.keySet()){
            List<Song> songlist = albumSongMap.get(a);
            List<Song> updatedSongs1 = new ArrayList<>();
            for(Song s: songlist){
                if(s.getTitle().equals(title)){
                    song = s;
                }
                else{
                    updatedSongs1.add(s);
                }
            }
            songlist = updatedSongs1;
            albumSongMap.put(a, songlist);
        }

        for(Playlist p: playlistSongMap.keySet()){
            List<Song> songlist = playlistSongMap.get(p);
            List<Song> updatedSongs1 = new ArrayList<>();
            for(Song s: songlist){
                if(s.getTitle().equals(title)){
                    song = s;
                }
                else{
                    updatedSongs1.add(s);
                }
            }
            songlist = updatedSongs1;
            playlistSongMap.put(p, songlist);
        }

        if(songLikeMap.containsKey(song)){
            songLikeMap.remove(song);
        }
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user = null;

        for(User u: users){
            if(u.getMobile().equals(mobile)){
                user = u;
                break;
            }
        }

        if(user == null){
            throw new Exception("User does not exist");
        }

        Song song = null;

        for(Song s: songs){
            if(s.getTitle().equals(songTitle)){
                song = s;
                break;
            }
        }

        if(song == null){
            throw new Exception("Song does not exist");
        }

        if(songLikeMap.containsKey(song)){
            List<User> userlist = songLikeMap.get(song);
            Boolean present = false;
            for(User u: userlist){
                if(u.getMobile().equals(mobile)){
                    present = true;
                    break;
                }
            }
            if(present == false){
                userlist.add(user);
                song.setLikes(song.getLikes()+1);
                for(Artist art: artists){
                    for(Album alb: artistAlbumMap.get(art)){
                        for(Song son: albumSongMap.get(alb)){
                            if(son.getTitle().equals(songTitle)){
                                art.setLikes(art.getLikes()+1);
                            }
                        }
                    }
                }
            }
            songLikeMap.put(song, userlist);
        }
        else {
            List<User> userList = new ArrayList<>();
            userList.add(user);
            song.setLikes(song.getLikes() + 1);
            for (Artist art : artists) {
                for (Album alb : artistAlbumMap.get(art)) {
                    for (Song son : albumSongMap.get(alb)) {
                        if (son.getTitle().equals(songTitle)) {
                            art.setLikes(art.getLikes() + 1);
                        }
                    }
                }
            }
            songLikeMap.put(song, userList);
        }
        return song;
    }

    public String mostPopularArtist() {
        Artist artist = null;
        int likes = 0;
        for(Artist a: artists){
            if(a.getLikes()>likes){
                artist = a;
                likes = a.getLikes();
            }
        }
        return artist.getName();
    }

    public String mostPopularSong() {
        Song song = null;
        int likes = 0;
        for(Song a: songs){
            if(a.getLikes()>likes){
                song = a;
                likes = a.getLikes();
            }
        }
        return song.getTitle();
    }
}