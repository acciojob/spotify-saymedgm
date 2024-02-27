package com.driver;

import java.util.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("spotify")
public class SpotifyController {

    //Autowire will not work in this case, no need to change this and add autowire
    SpotifyService spotifyService = new SpotifyService();

    @PostMapping("/add-user")
    public String createUser(@RequestParam(name = "name") String name, String mobile) throws Exception {
        //If the mobile number exists in database, throw "User already exists" exception
        //Otherwise, create the user
        User user = spotifyService.createUser(name, mobile);
        return "Success";
    }

    @PostMapping("/add-artist")
    public String createArtist(@RequestParam(name = "name") String name) throws Exception{
        //If the name exists in database, throw "Artist already exists" exception
        //Otherwise, create the artist

        Artist artist = spotifyService.createArtist(name);
        return "Success";
    }

    @PostMapping("/add-album")
    public String createAlbum(@RequestParam(name = "title") String title, String artistName){
        //If the artist does not exist, create an artist with given name
        //Create an album (it is guaranteed that it does not exist and name is unique)

        Album album = spotifyService.createAlbum(title, artistName);
        return "Success";
    }

    @PostMapping("/add-song")
    public String createSong(String title, String albumName, int length) throws Exception{
        //If the album does not exist in database, throw "Album does not exist" exception
        //Create and add the song to respective album and return the number of songs in the updated album

        Song song = spotifyService.createSong(title, albumName, length);
        return "Success";
    }

    @PostMapping("/add-playlist-on-length")
    public String createPlaylistOnLength(String mobile, String title, int length) throws Exception{
        //Create a playlist with given title and add all songs having the given length in the database to that playlist
        //The creater of the playlist will be the given user and will also be the only listener at the time of playlist creation
        //If the user does not exist, throw "User does not exist" exception
        //Return the number of songs in the playlist

        Playlist playlist = spotifyService.createPlaylistOnLength(mobile, title, length);
        return "Success";
    }

    @PostMapping("/add-playlist-on-name")
    public String createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception{
        //Create a playlist with given title and add all songs having the given titles in the database to that playlist
        //The creater of the playlist will be the given user and will also be the only listener at the time of playlist creation
        //If the user does not exist, throw "User does not exist" exception
        //Return the number of songs in the playlist

        Playlist playlist = spotifyService.createPlaylistOnName(mobile, title, songTitles);
        return "Success";
    }
    // Playlist -> List<Artist>: List<Song> -> List<Albums> -> List<Artists>

    @PutMapping("/find-playlist")
    public String findPlaylist(String mobile, String playlistTitle) throws Exception{
        //Find the playlist with given title and add user as listener of that playlist and update user accordingly
        //If the user is creater or already a listener, do nothing
        //If the user does not exist, throw "User does not exist" exception
        //If the playlist does not exists, throw "Playlist does not exist" exception
        //Return the number of listeners of the playlist

        Playlist playlist = spotifyService.findPlaylist(mobile, playlistTitle);
        return "Success";
    }

    @PutMapping("/like-song")
    public String likeSong(String mobile, String songTitle) throws Exception{
        //The mentioned user likes the given song as well and the corresponding artist gets auto-liked
        //A song can be liked by a user atmost once.
        //However, an artist can indirectly have multiple likes from a user, if the user has liked multiple songs of that artist.
        //If the user does not exist, throw "User does not exist" exception
        //Return the number of likes of the song

        Song song = spotifyService.likeSong(mobile, songTitle);
        return "Success";
    }

    @GetMapping("/popular-artist")
    public String mostPopularArtist(){
        //Return the artist name with maximum likes
        return spotifyService.mostPopularArtist();
    }

    @GetMapping("/popular-song")
    public String mostPopularSong(){
        //return the song title with maximum likes
        return spotifyService.mostPopularSong();
    }

    @DeleteMapping("/remove-song")
    public String removeSong(String title) throws Exception{
        //Remove the song from all albums and playlists
        //Return the number of remaining songs in the corresponding album after deleting the song

        spotifyService.removeSong(title);
        return "Success";
    }
}